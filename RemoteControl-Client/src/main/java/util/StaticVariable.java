package util;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import listener.User32jna;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.util.Vector;

public class StaticVariable {
    public static Vector<byte[]> imgVec = new Vector<>();
    public static BufferedImage screenImage;
    public static int image_Width = 1280;
    public static int image_Height = 720;
    public static final Object lock = new Object();
    public static int FPSCount = 0;
    public static BufferedImage image;

    public static JLabel FPSLabel;
    public static DataOutputStream keyBoardOutputStream;
    public static User32jna u32 = User32jna.INSTANCE;
    public static User32 lib = User32.INSTANCE;

    public static WinUser.HHOOK hhk = null;
    public static WinDef.HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);

}
