package simulation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Utilities {

    static long timestep = 100000000L;

    static void Wait() {
        long goal = System.nanoTime () + timestep;
        while (System.nanoTime () < goal)
            Thread.yield ();
    }

    static boolean stop = false;
    static boolean loadImage = false;
    static boolean saveImage = false;
    static int img_num = 0;
    static File saved_image;
    static String img_location = "C://Users//LENOVO//Pictures//cthulu.jpeg"; //D:\\Pictures_default\\Cool backgrounds\\doom.jpg";

    static void saveImage(BufferedImage image) {
        saved_image = new File (String.valueOf (Utilities.img_num++));
        try {
            ImageIO.write (image, "PNG", saved_image);
        } catch (IOException e) {
            e.printStackTrace ();
        }
        saveImage = false;
    }

    static BufferedImage resize(BufferedImage old_img, int width, int height) {
        BufferedImage new_img = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = new_img.getGraphics ();
        g.drawImage (old_img, 0, 0, width, height, null);
        g.dispose ();
        return new_img;
    }

    public static void setLoadImage(boolean loadImage) {
        Utilities.loadImage = loadImage;
        stop = true;
    }

    public static void setSaveImage(boolean saveImage) {
        Utilities.saveImage = saveImage;
    }
}
