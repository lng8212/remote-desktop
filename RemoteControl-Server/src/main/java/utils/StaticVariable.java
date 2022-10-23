package utils;

import listener.User32jna;
import view.MainPanel;
import view.NetworkScreenServer;

import java.awt.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class StaticVariable {
    public static ServerSocket keyboardServerSocket = null;
    public static Socket keyboardSocket = null;
    public static Socket cursorSocket = null;
    public static ServerSocket cursorServerSocket = null;

    public static ServerSocket imageSeverSocket = null;
    public static User32jna u32 = User32jna.INSTANCE;
    public static Robot robot;
    public static Boolean isRunning = false;
    public static Rectangle rect;
    public static Vector<byte[]> imgvec = new Vector<>();
    public static MainPanel mainPanel = new MainPanel();
}
