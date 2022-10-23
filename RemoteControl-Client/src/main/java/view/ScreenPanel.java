package view;

import debug.DebugMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.*;
import java.net.Socket;
import thread.FPSCheckThread;
import thread.KeyboardThread;
import thread.ShowThread;
import util.Constant;
import util.StaticVariable;

import static util.StaticVariable.*;

class ScreenPanel extends JPanel implements Runnable {
	private Socket socket;
	private Socket cursorSocket;
	private Socket keyboardSocket;
	private JFrame frame;

	private DataOutputStream mouseOutputStream;
	private DataInputStream dataInputStream;
	private ObjectInputStream objectInputStream;
	private BufferedWriter bufferedWriter;
	private byte imageByte2[] = new byte[6220800];
	private int mouseX = 0, mouseY = 0;
	private int mouseClickCount = 0;
	private int mouseButton = 0;
	private int mousePosition = 0; // 1 == move 2 == click
	private int screen_Width = 1920;
	private int screen_Height = 1080;
	private Boolean isCompress = true;

	private int count = 0;

	ScreenPanel ppp = this;
	Thread thread;




	public ScreenPanel(JFrame frame, Socket socket, Socket cursorSocket, Socket keyboardSocket) {
		setLayout(null);
		this.socket = socket;
		this.cursorSocket = cursorSocket;
		this.keyboardSocket = keyboardSocket;
		this.frame = frame;
		FPSLabel = new JLabel("FPS : " + FPSCount);
		FPSLabel.setFont(new Font(Constant.myFont, Font.BOLD, 20));
		FPSLabel.setBounds(10, 10, 100, 50);
		add(FPSLabel);
		System.out.println(socket.getInetAddress());
		try {
			setLayout(null);
			socket.setTcpNoDelay(true);
			/*
			 * socket.setSendBufferSize(1024 * 1024 * 10);
			 * socket.setReceiveBufferSize(1024 * 1024 * 10);
			 */
			mouseOutputStream = new DataOutputStream(cursorSocket.getOutputStream());
			keyBoardOutputStream = new DataOutputStream(keyboardSocket.getOutputStream());
			dataInputStream = new DataInputStream(socket.getInputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			DebugMessage.printDebugMessage(e);
		}
		thread = new Thread(this);
		thread.start();
		KeyboardThread keyboardThread = new KeyboardThread();
		keyboardThread.start();
		FPSCheckThread fpsCheckThread = new FPSCheckThread();
		fpsCheckThread.start();
		ShowThread ss = new ShowThread(() -> repaint());
		//showThread ss2 = new showThread();
		ss.start();
		//ss2.start();
		// lib.UnhookWindowsHookEx(hhk);
		addMouseWheelListener(e -> {
			try {
				int n = e.getWheelRotation();
				if (n < 0) {
					mouseOutputStream.writeInt(Constant.MOUSE_DOWN_WHEEL);
				} else {
					mouseOutputStream.writeInt(Constant.MOUSE_UP_WHEEL);
				}

			} catch (IOException e1) {
				DebugMessage.printDebugMessage(e1);
			}
		});
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// 800 300 1920 1080
				// x y x y
				mouseX = e.getX() * screen_Width / getWidth();
				mouseY = e.getY() * screen_Height / getHeight();

				try {
					mouseOutputStream.writeInt(Constant.MOUSE_MOVE);
					mouseOutputStream.writeInt(mouseX);
					mouseOutputStream.writeInt(mouseY);
				} catch (IOException e1) {
					DebugMessage.printDebugMessage(e1);
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mouseX = e.getX() * screen_Width / getWidth();
				mouseY = e.getY() * screen_Height / getHeight();
				try {
					mouseOutputStream.writeInt(Constant.MOUSE_MOVE);
					mouseOutputStream.writeInt(mouseX);
					mouseOutputStream.writeInt(mouseY);
				} catch (IOException e1) {
					DebugMessage.printDebugMessage(e1);
				}

			}
		});
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				mouseButton = e.getButton();
				try {
					mouseOutputStream.writeInt(Constant.MOUSE_PRESSED);
					mouseOutputStream.writeInt(mouseButton);
				} catch (IOException e1) {
					DebugMessage.printDebugMessage(e1);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseButton = e.getButton();
				try {
					mouseOutputStream.writeInt(Constant.MOUSE_RELEASED);
					mouseOutputStream.writeInt(mouseButton);
				} catch (IOException e1) {
					DebugMessage.printDebugMessage(e1);
				}
			}
		});

		requestFocus();
	}

	@Override
	public void run() {
		try {
			screen_Width = dataInputStream.readInt();
			screen_Height = dataInputStream.readInt();
			StaticVariable.image_Width = dataInputStream.readInt();
			StaticVariable.image_Height = dataInputStream.readInt();
			isCompress = dataInputStream.readBoolean();
		} catch (IOException e1) {
			DebugMessage.printDebugMessage(e1);
		}

		while (true) {

			try {// 172.30.1.54
				if (dataInputStream.available() > 0) {
					int length;
					if (isCompress) {

						length = dataInputStream.readInt();

						byte imageByte[] = new byte[length];

						dataInputStream.readFully(imageByte, 0, length);

						StaticVariable.imgVec.addElement(imageByte);
						//System.out.println(imgVec.size());

						if (StaticVariable.imgVec.size() > 1) {
							synchronized (StaticVariable.lock) {

								StaticVariable.lock.wait();
							}
						}

					} else {
						length = dataInputStream.readInt();
						dataInputStream.readFully(imageByte2, 0, length);
						StaticVariable.screenImage = new BufferedImage(StaticVariable.image_Width, StaticVariable.image_Height, BufferedImage.TYPE_3BYTE_BGR);
						StaticVariable.screenImage.setData(Raster.createRaster(StaticVariable.screenImage.getSampleModel(),
								new DataBufferByte(imageByte2, imageByte2.length), new Point()));
					}
					//thread.sleep(20);
				}

			} catch (Exception e) {
				//debug.DebugMessage.printDebugMessage(e);
			}

		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}
}
