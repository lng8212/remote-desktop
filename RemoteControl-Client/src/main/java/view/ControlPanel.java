package view;

import debug.DebugMessage;
import util.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ControlPanel extends JPanel {
    JTextField addressField = new JTextField(10);
    JButton connectBtn = new JButton("Connect");
    JButton exitBtn = new JButton("Exit");
    private Socket socket = new Socket();
    private Socket cursorSocket = new Socket();
    private Socket keyboardSocket = new Socket();

    private JFrame jFrame;

    ScreenPanel screenPanel;
    public ControlPanel(JFrame jFrame) {
        setLayout(null);
        this.jFrame = jFrame;
        addressField.setBounds(100, 250, 300, 50);
        connectBtn.setBounds(400, 250, 150, 50);
        exitBtn.setBounds(550, 250, 150, 50);
        addressField.setFont(new Font(Constant.myFont, Font.PLAIN, 20));
        connectBtn.setFont(new Font(Constant.myFont, Font.PLAIN, 20));
        exitBtn.setFont(new Font(Constant.myFont, Font.PLAIN, 20));
        addressField.setForeground(Color.LIGHT_GRAY);
        addressField.setText("123.123.123.123");
        addressField.setCaretPosition(0);
        addressField.setMargin(new Insets(1, 15, 1, 15));
        addressField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if(addressField.getText().equals("123.123.123.123") && addressField.getForeground() == Color.LIGHT_GRAY){
                    addressField.setText("");
                    addressField.setForeground(Color.BLACK);
                }
                else if(addressField.getText().equals("Connect Fail")){
                    addressField.setText("");
                    addressField.setForeground(Color.BLACK);
                }
            }
        });
        addressField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(addressField.getText().equals("")){
                    addressField.setForeground(Color.LIGHT_GRAY);
                    addressField.setText("123.123.123.123");
                    addressField.setCaretPosition(0);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    connectBtn.doClick();
                }
                if(addressField.getText().equals("123.123.123.123") && addressField.getForeground() == Color.LIGHT_GRAY){
                    addressField.setText("");
                    addressField.setForeground(Color.BLACK);
                }

            }
        });

        add(addressField);
        add(connectBtn);
        add(exitBtn);
        exitBtn.setEnabled(false);
        connectBtn.addActionListener(e -> {
                /*GraphicsDevice device = GraphicsEnvironment
                        .getLocalGraphicsEnvironment().getScreenDevices()[0];
                device.setFullScreenWindow(jFrame);*/

            InetSocketAddress inetAddress;
            InetSocketAddress inetCursorAddress;
            InetSocketAddress inetKeyboardAddress;
            if(addressField.getText().equals("123.123.123.123") && addressField.getForeground() == Color.LIGHT_GRAY){
                inetAddress = new InetSocketAddress("localhost", Constant.SERVER_PORT);
                inetCursorAddress = new InetSocketAddress("localhost", Constant.SERVER_CURSOR_PORT);
                inetKeyboardAddress = new InetSocketAddress("localhost", Constant.SERVER_KEYBOARD_PORT);
            }
            else{
                inetAddress = new InetSocketAddress(addressField.getText(), Constant.SERVER_PORT);
                inetCursorAddress = new InetSocketAddress(addressField.getText(), Constant.SERVER_CURSOR_PORT);
                inetKeyboardAddress = new InetSocketAddress(addressField.getText(), Constant.SERVER_KEYBOARD_PORT);
            }
            try {
                socket.connect(inetAddress, 1000);
                cursorSocket.connect(inetCursorAddress, 1000);
                keyboardSocket.connect(inetKeyboardAddress,1000);
            } catch (IOException e1) {
                DebugMessage.printDebugMessage(e1);
                addressField.setText("Connect Fail");
                socket = new Socket();
                cursorSocket = new Socket();
                keyboardSocket = new Socket();
            }
            if(socket.isConnected()){
                addressField.setText("Connect Success!");
                System.out.println("Connected");
                try {
                    Thread.sleep(500);
                    screenPanel = new ScreenPanel(jFrame, socket, cursorSocket, keyboardSocket);

                    jFrame.setContentPane(screenPanel);
                    screenPanel.requestFocus();
                    //setExtendedState(JFrame.MAXIMIZED_BOTH);
                    jFrame.revalidate();
                    screenPanel.requestFocus();


                } catch (InterruptedException e1) {
                    DebugMessage.printDebugMessage(e1);
                }
            }

        });
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
    }
}