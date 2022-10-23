package view;

import thread.CursorThread;
import thread.ImgDoubleBufferTh;
import thread.KeyBoardThread;
import thread.ServerSocketCloseThread;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import static utils.Constant.*;
import static utils.StaticVariable.*;
import static utils.StaticVariable.imgvec;

public class MainPanel extends JPanel implements Runnable {
    private DataOutputStream dataOutputStream;
    private ObjectOutputStream objectOutputStream;
    private Image cursor;
    private String myFont = "????";
    private JButton startBtn;
    private JButton stopBtn;
    private JTextField widthTextfield;
    private JTextField heightTextfield;
    private JRadioButton compressTrueRBtn;
    private JRadioButton compressFalseRBtn;
    private JLabel widthLabel;
    private JLabel heightLabel;
    private JLabel compressLabel;

    private Thread imgThread;
    private Boolean isCompress = true;

    private int screenWidth, screenHeight;
    private Socket imageSocket = null;
    private URL cursorURL = getClass().getClassLoader().getResource("cursor.gif");

    public MainPanel() {
        setLayout(null);

        startBtn = new JButton("Start");
        stopBtn = new JButton("Stop");
        widthTextfield = new JTextField(Integer.toString(new_Width), 5);
        heightTextfield = new JTextField(Integer.toString(new_Height), 5);
        widthLabel = new JLabel("width");
        heightLabel = new JLabel("height");
        compressLabel = new JLabel("<html>&nbsp&nbsp&nbsp<span>Image<br>Compress</span></html>");
        compressTrueRBtn = new JRadioButton("True");
        compressFalseRBtn = new JRadioButton("False");

        startBtn.setBounds(0, 0, 150, 130);
        stopBtn.setBounds(150, 0, 150, 130);
        widthLabel.setBounds(327, 8, 50, 15);
        widthTextfield.setBounds(300, 30, 90, 35);
        heightLabel.setBounds(325, 70, 50, 15);
        heightTextfield.setBounds(300, 90, 90, 35);
        compressLabel.setBounds(405, -10, 100, 50);
        compressTrueRBtn.setBounds(390, 30, 80, 30);
        compressFalseRBtn.setBounds(390, 90, 80, 30);

        ButtonGroup group = new ButtonGroup();
        group.add(compressTrueRBtn);
        group.add(compressFalseRBtn);

        widthLabel.setFont(new Font(myFont, Font.PLAIN, 15));
        heightLabel.setFont(new Font(myFont, Font.PLAIN, 15));

        compressLabel.setFont(new Font(myFont, Font.PLAIN, 10));
        startBtn.setFont(new Font(myFont, Font.PLAIN, 20));
        stopBtn.setFont(new Font(myFont, Font.PLAIN, 20));
        compressTrueRBtn.setFont(new Font(myFont, Font.PLAIN, 20));
        compressFalseRBtn.setFont(new Font(myFont, Font.PLAIN, 20));

        compressTrueRBtn.setSelected(true);

        add(startBtn);
        add(stopBtn);
        add(widthLabel);
        add(widthTextfield);
        add(heightLabel);
        add(heightTextfield);
        add(compressLabel);
        add(compressTrueRBtn);
        add(compressFalseRBtn);
        stopBtn.setEnabled(false);
        startBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning)
                    return;
                try {
                    new_Height = Integer.parseInt(heightTextfield.getText());
                    new_Width = Integer.parseInt(widthTextfield.getText());
                } catch (Exception e1) {
                    return;
                }
                heightTextfield.setEditable(false);
                widthTextfield.setEditable(false);
                isRunning = true;
                startBtn.setEnabled(false);
                stopBtn.setEnabled(true);
                if (compressTrueRBtn.isSelected()) {
                    isCompress = true;
                } else if (compressFalseRBtn.isSelected()) {
                    isCompress = false;
                }
                compressTrueRBtn.setEnabled(false);
                compressFalseRBtn.setEnabled(false);

                imgThread = new Thread(mainPanel);
                CursorThread cursorThread = new CursorThread();
                KeyBoardThread keyBoardThread = new KeyBoardThread();
                imgThread.start();
                cursorThread.start();
                keyBoardThread.start();

            }
        });
        stopBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRunning)
                    return;
                heightTextfield.setEditable(true);
                widthTextfield.setEditable(true);
                isRunning = false;
                ServerSocketCloseThread closeThread = new ServerSocketCloseThread();
                closeThread.start();
                // imgThread.interrupt();
                stopBtn.setEnabled(false);
                startBtn.setEnabled(true);
                compressTrueRBtn.setEnabled(true);
                compressFalseRBtn.setEnabled(true);

            }
        });
        widthTextfield.transferFocus();
    }

    public void run() {

        try {
            robot = new Robot();
            screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
            screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
            rect = new Rectangle(0, 0, screenWidth, screenHeight);
            imageSeverSocket = new ServerSocket(SERVER_PORT);// ImageSERVER

            imageSocket = imageSeverSocket.accept();
            imageSocket.setTcpNoDelay(true);
            dataOutputStream = new DataOutputStream(imageSocket.getOutputStream());
            objectOutputStream = new ObjectOutputStream(imageSocket.getOutputStream());
            dataOutputStream.writeInt(screenWidth);
            dataOutputStream.writeInt(screenHeight);
            dataOutputStream.writeInt(new_Width);
            dataOutputStream.writeInt(new_Height);
            dataOutputStream.writeBoolean(isCompress);
            cursor = ImageIO.read(cursorURL);
        } catch (Exception e) {

        }
        ImgDoubleBufferTh th = new ImgDoubleBufferTh();
        th.start();

        //new ImgDoubleBufferTh().start();

        // ImageIO.setUseCache(false);// little more spped

        // imgvec.add(getScaledImage(robot.createScreenCapture(rectangle),screenWidth,screenHeight,BufferedImage.TYPE_3BYTE_BGR));
        // Image cursor = ImageIO.read(new
        // File("c:\\Test\\cursor.gif"));

        // long starttime,estimatedTime;
        int index = 0;
        Runtime runtime = Runtime.getRuntime();
        while (isRunning) {
            try {

                // screenImage =
                // JNAScreenShot.getScreenshot(rectangle);//robot.createScreenCapture(rectangle);
                // if (img[index] != null) {
                // screenImage = img[index];//
                // robot.createScreenCapture(rectangle);

                //screenImage =imgvec.get(0); //robot.createScreenCapture(rect);//
                byte[] imageByte = imgvec.get(0);
                if(imgvec.size() == 3){
                    synchronized (th) {
                        th.notify();
                    }
                }
                if (isCompress) {

                    //byte[] compressImageByte = compress(imageByte);// 6MB->480KB????
                    // System.out.println("compress : " +
                    // (double)compressImageByte.length/1024 + "kb");
                    dataOutputStream.writeInt(imageByte.length);
                    dataOutputStream.write(imageByte);

                    // System.out.println(imageByte.length);
                    dataOutputStream.flush();
                } else {
                    dataOutputStream.writeInt(imageByte.length);
                    dataOutputStream.write(imageByte);
                    // System.out.println(imageByte.length);

                    dataOutputStream.flush();
                }
                //}
            } catch (Exception e) {

            }
            if (runtime.totalMemory() / 1024 / 1024 > 500)
                System.gc();
            if (imgvec.size() > 1) {
					/*		new Thread(){
								public void run(){						*/
                //System.out.println(imgvec.size());
                imgvec.remove(0);
                index++;
                if(index == 30){
                    index=0;
                    System.gc();
                }
						/*		}
							}.start();*/
            }
            // Thread.sleep(1000);
        }
    }
}