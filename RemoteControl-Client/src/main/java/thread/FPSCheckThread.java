package thread;

import static util.StaticVariable.FPSCount;
import static util.StaticVariable.FPSLabel;

public class FPSCheckThread extends Thread {
    public void run() {
        while (true) {
            try {
                sleep(1000);
                FPSLabel.setText("FPS : " + Integer.toString(FPSCount));
                // repaint();
                // System.out.println("FPS : " + FPScount);
                FPSCount = 0;
            } catch (InterruptedException e) {
            }
        }
    }
}