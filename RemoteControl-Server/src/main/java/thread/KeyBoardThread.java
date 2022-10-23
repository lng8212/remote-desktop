package thread;

import java.io.DataInputStream;
import java.net.ServerSocket;

import static utils.Constant.SERVER_KEBOARD_PORT;
import static utils.StaticVariable.*;

public class KeyBoardThread extends Thread {
    private final int KEY_PRESSED = 6;
    private final int KEY_RELEASED = 7;
    public void run() {
        try {
            keyboardServerSocket = new ServerSocket(SERVER_KEBOARD_PORT);
            keyboardSocket = keyboardServerSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(keyboardSocket.getInputStream());
            while (true) {
                int keyboardState = dataInputStream.readInt();
                if (keyboardState == KEY_PRESSED) {// KEYBOARD PRESSED
                    int keyCode = dataInputStream.readInt();
                    // System.out.println(keyCode + "????");
                    u32.keybd_event((byte) keyCode, (byte) 0, 0, 0);// ????ffDDDddSS
                    // robot.keyPress(keyCode);
                } else if (keyboardState == KEY_RELEASED) {
                    int keyCode = dataInputStream.readInt();
                    // System.out.println(keyCode + "????");
                    u32.keybd_event((byte) keyCode, (byte) 00, (byte) 0x0002, 0);// ??
                    // robot.keyRelease(keyCode);
                }
                yield();
            }
        } catch (Exception e) {

        }
    }
}