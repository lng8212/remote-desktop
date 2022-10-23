package thread;

import static util.StaticVariable.FPSCount;
import static util.StaticVariable.FPSLabel;

public class FPSCheckThread extends Thread {
    public void run() {
        while (true) {
            try {
                sleep(1000);
                FPSLabel.setText("FPS : " + FPSCount);
                FPSCount = 0;
            } catch (InterruptedException e) {
            }
        }
    }
}