package thread;

import listener.RepaintCallBack;
import org.xerial.snappy.Snappy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

import static util.StaticVariable.*;

public class ShowThread extends Thread {
    private final RepaintCallBack repaintCallBack;
    public ShowThread(RepaintCallBack repaintCallBack) {
        this.repaintCallBack = repaintCallBack;
    }
    public void run() {

        while (true) {
            try {

                byte[] imageByte = imgVec.get(0);
                byte[] uncompressImageByte = Snappy.uncompress(imageByte);
                if (imgVec.size() > 0){
                    imgVec.remove(0);
                }
                screenImage = new BufferedImage(image_Width, image_Height, BufferedImage.TYPE_3BYTE_BGR);
                screenImage.setData(Raster.createRaster(screenImage.getSampleModel(),
                        new DataBufferByte(uncompressImageByte, uncompressImageByte.length), new Point()));
                if (imgVec.size() == 1) {
                    synchronized (lock) {
                        lock.notify();
                    }
                }
                if(screenImage != null){
                    image = screenImage;
                    FPSCount++;
                    repaintCallBack.onInvoke();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
