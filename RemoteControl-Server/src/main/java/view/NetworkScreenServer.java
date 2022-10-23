package view;

import com.sun.jna.Library;
import com.sun.jna.Native;
import debug.DebugMessage;
import listener.User32jna;
import org.xerial.snappy.Snappy;
import thread.CursorThread;
import thread.ImgDoubleBufferTh;
import thread.KeyBoardThread;
import thread.ServerSocketCloseThread;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Vector;

import static utils.Constant.SERVER_PORT;
import static utils.StaticVariable.*;


public class NetworkScreenServer extends JFrame {
	private JTextField widthTextfield;
	int count = 0, count2 = 0;
	private int buffersize = 1;
	private BufferedImage[] img = new BufferedImage[buffersize];

	public NetworkScreenServer() {
		setTitle("view.NetworkScreenServer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setContentPane(mainPanel);
		setSize(490, 160);
		setVisible(true);
		setResizable(false);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("type" + e.getKeyCode() + "  " + e.getKeyChar() + "  " + e.getID() + "  "
						+ e.getModifiers() + "  " + e.getKeyLocation() + "  " + e.getExtendedKeyCode());
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("pressed" + e.getKeyCode() + "  " + e.getKeyChar() + "  " + e.getID() + "  "
						+ e.getModifiers() + "  " + e.getKeyLocation() + "  " + e.getExtendedKeyCode());
				super.keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("released" + e.getKeyCode() + "  " + e.getKeyChar() + "  " + e.getID() + "  "
						+ e.getModifiers() + "  " + e.getKeyLocation() + "  " + e.getExtendedKeyCode());
				if (e.getKeyCode() == 0) {
					if (count >= 1) {
						count = 0;
						return;
					}
					// System.out.println(t.getLocale().toString() + " " +
					// t.getLocale().getCountry() + " " +
					// t.getLocale().getDisplayCountry());
					System.out.println("ee");
					count = 1;
					u32.keybd_event((byte) 0x15, (byte) 0, 0, 0);// ????ffDDDddSS
					u32.keybd_event((byte) 0x15, (byte) 00, (byte) 0x0002, 0);// ????
																				// ????
				}
			}
		});
		widthTextfield.requestFocus();
	}

	/*
	 * User32jna u32 = User32jna.INSTANCE;
	 */

}
