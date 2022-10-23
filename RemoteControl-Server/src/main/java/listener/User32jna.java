package listener;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface User32jna extends Library {
    User32jna INSTANCE = (User32jna) Native.load("user32.dll", User32jna.class);

    // User32jna INSTANCE = (User32jna)
    // Native.loadLibrary("user32.dll",User32jna.class);
    public void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);
}