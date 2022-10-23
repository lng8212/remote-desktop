package thread;

import debug.DebugMessage;

import java.io.IOException;

import static utils.StaticVariable.*;

public class ServerSocketCloseThread extends Thread {
    public void run() {
        if (!imageSeverSocket.isClosed() || !cursorServerSocket.isClosed() || keyboardServerSocket.isClosed()) {
            try {
                imageSeverSocket.close();
                cursorServerSocket.close();
                keyboardServerSocket.close();
            } catch (IOException e) {
                DebugMessage.printDebugMessage(e);
            }
        }
    }
}
