package thread;

import org.xerial.snappy.Snappy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

import static utils.Constant.new_Height;
import static utils.Constant.new_Width;
import static utils.StaticVariable.imgvec;
import static utils.StaticVariable.rect;

public	class ImgDoubleBufferTh extends Thread {
    BufferedImage bufferimage;
    Robot robot = null;

    synchronized public void run() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
        }
        while (true) {

            bufferimage = robot.createScreenCapture(rect);
            bufferimage = getScaledImage(bufferimage, new_Width, new_Height, BufferedImage.TYPE_3BYTE_BGR);
            byte[] imageByte = ((DataBufferByte) bufferimage.getRaster().getDataBuffer()).getData();
            try {
                imgvec.addElement(compress(imageByte));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(imgvec.size());
            if(imgvec.size()>5)
                try {
                    //System.out.println("wait");
                    wait();
                } catch (InterruptedException e) {

                }
        }

    }

    public static byte[] compress(byte[] data) throws IOException {
        byte[] output = Snappy.compress(data);

        return output;
    }

    public BufferedImage getScaledImage(BufferedImage myImage, int width, int height, int type) {
        BufferedImage background = new BufferedImage(width, height, type);
        Graphics2D g = background.createGraphics();
        g.setColor(Color.WHITE);
        g.drawImage(myImage, 0, 0, width, height, null);
        g.dispose();
        return background;
    }
}
