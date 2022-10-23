package thread;


import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import debug.DebugMessage;
import util.Constant;

import static util.StaticVariable.*;

public class KeyboardThread extends Thread {
    private int result;
    private int keypress = 0;


    public void run() {
        WinUser.LowLevelKeyboardProc rr = (nCode, wParam, info) -> {
            try {
                System.out.println(info.vkCode);
                if (info.vkCode == 21) {
                    if (keypress == 0) {
                        u32.keybd_event((byte) 0x15, (byte) 0, 0, 0);//ffDDDddSS
                        u32.keybd_event((byte) 0x15, (byte) 00, (byte) 0x0002, 0);
                        keypress++;
                    } else {
                        keypress = 0;
                    }

                }
                if (nCode >= 0) {

                    switch (wParam.intValue()) {
                        case WinUser.WM_KEYUP:
                        case WinUser.WM_SYSKEYUP:
                            keyBoardOutputStream.writeInt(Constant.KEY_RELEASED);
                            keyBoardOutputStream.writeInt(info.vkCode);
                            break;
                        case WinUser.WM_KEYDOWN:
                        case WinUser.WM_SYSKEYDOWN:
                            keyBoardOutputStream.writeInt(Constant.KEY_PRESSED);
                            keyBoardOutputStream.writeInt(info.vkCode);
                            break;
                    }

                }
            } catch (Exception e) {
                DebugMessage.printDebugMessage(e);
            }

            Pointer ptr = info.getPointer();

            long peer = Pointer.nativeValue(ptr);

            return lib.CallNextHookEx(hhk, nCode, wParam, new WinDef.LPARAM(peer));
        };
        hhk = lib.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, rr, hMod, 0);
        WinUser.MSG msg = new WinUser.MSG();
        while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                System.err.println("error in get message");
                break;
            } else {
                System.err.println("got message");
                lib.TranslateMessage(msg);
                lib.DispatchMessage(msg);
            }
        }
    }
}
