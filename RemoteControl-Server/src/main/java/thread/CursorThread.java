package thread;

import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.net.ServerSocket;

import static utils.Constant.SERVER_CURSOR_PORT;
import static utils.StaticVariable.*;

public class CursorThread extends Thread {
    private final int MOUSE_MOVE = 1;
    private final int MOUSE_PRESSD = 2;
    private final int MOUSE_RELEASED = 3;
    private final int MOUSE_DOWN_WHEEL = 4;
    private final int MOUSE_UP_WHEEL = 5;
    public void run() {
        try {
            cursorServerSocket = new ServerSocket(SERVER_CURSOR_PORT);// cursorSERVER
            cursorSocket = cursorServerSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(cursorSocket.getInputStream());
            int mouseX = 0;
            int mouseY = 0;
            while (isRunning) {
                int mouseState = dataInputStream.readInt();// mouse,Keyboard
                // state
                if (mouseState == MOUSE_MOVE) {// move
                    mouseX = dataInputStream.readInt();
                    mouseY = dataInputStream.readInt();

                    robot.mouseMove(mouseX, mouseY);
                } else if (mouseState == MOUSE_PRESSD) { // pressed
                    int mouseButton = dataInputStream.readInt();
                    robot.mouseMove(mouseX, mouseY);
                    if (mouseButton == 1) {
                        robot.mousePress(MouseEvent.BUTTON1_MASK);
                    } else if (mouseButton == 2) {
                        robot.mousePress(MouseEvent.BUTTON2_MASK);
                    } else if (mouseButton == 3) {
                        robot.mousePress(MouseEvent.BUTTON3_MASK);
                    }
                } else if (mouseState == MOUSE_RELEASED) {// released
                    int mouseButton = dataInputStream.readInt();
                    robot.mouseMove(mouseX, mouseY);
                    if (mouseButton == 1) {
                        robot.mouseRelease(MouseEvent.BUTTON1_MASK);
                    } else if (mouseButton == 2) {
                        robot.mouseRelease(MouseEvent.BUTTON2_MASK);
                    } else if (mouseButton == 3) {
                        robot.mouseRelease(MouseEvent.BUTTON3_MASK);
                    }
                } else if (mouseState == MOUSE_DOWN_WHEEL) {// MOUSE DOWN
                    // WHEEL
                    robot.mouseWheel(-3);
                } else if (mouseState == MOUSE_UP_WHEEL) {// MOUSE UP WHEEL
                    robot.mouseWheel(3);
                }
                yield();
            }

        } catch (Exception e) {

        }

    }
}
