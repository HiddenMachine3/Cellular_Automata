package simulation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.io.IOException;

public class Main {

    static double ratio = 1296.0 / 690;
    int GRID_HEIGHT = 500;
    int GRID_WIDTH = (int) (ratio * GRID_HEIGHT);

    int seed;
    Random random;

    Cell[][] Cells = new Cell[GRID_WIDTH][GRID_HEIGHT];
    Cell[][] Buffer = new Cell[GRID_WIDTH][GRID_HEIGHT];

    Window window;
    BufferedImage image;

    Main() {
//        initialiseRandom ();
        initialiseCustom ();
        window = new Window (Cells, GRID_WIDTH, GRID_HEIGHT);
    }

    void start() {

        for (; !Utilities.stop; ) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                for (int y = 0; y < GRID_HEIGHT; y++) {
                    int neighbouring = number_of_neighbours (x, y);

                    if (Cells[x][y].alive) {
                        if (neighbouring < 2 || neighbouring > 3) Buffer[x][y].alive = false;
                    } else if (neighbouring == 3) {
                        Buffer[x][y].alive = true;
                        Buffer[x][y].ResetColors ();
                    } else
                        Buffer[x][y].AdvanceColors ();
                }
            }
            copyBuffer ();
            window.panel1.repaint ();
            Utilities.Wait ();
        }

        if (Utilities.loadImage) {
            Utilities.loadImage = false;
            Utilities.stop = false;
            initialiseImage ();
            window.setCells (Cells);
            start ();
        }
    }

    int number_of_neighbours(int x, int y) {

        int total = 0;

        int right = (x + 1) % (GRID_WIDTH - 1);
        int left = (x - 1) < 0 ? GRID_WIDTH - 1 : x - 1;
        int down = (y + 1) % (GRID_HEIGHT - 1);
        int up = (y - 1) < 0 ? GRID_HEIGHT - 1 : y - 1;

        if (Cells[left][up].alive) total++;
        if (Cells[left][down].alive) total++;
        if (Cells[left][y].alive) total++;
        if (Cells[right][up].alive) total++;
        if (Cells[right][down].alive) total++;
        if (Cells[right][y].alive) total++;
        if (Cells[x][up].alive) total++;
        if (Cells[x][down].alive) total++;

        return total;
    }

    void copyBuffer() {
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                Cells[x][y].copyInformation (Buffer[x][y]);
            }
        }
    }

    void initialiseRandom() {
        seed = (int) (Math.random () * 10000000000000000.0);
        random = new Random (seed);
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                Cells[x][y] = new Cell (random.nextBoolean ());
                Buffer[x][y] = Cells[x][y].copy ();
            }
        }
    }

    void initialiseCustom() {
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                Cells[x][y] = new Cell (x == 0 || x == GRID_WIDTH - 1||y == 0 || y == GRID_HEIGHT - 1);
                Buffer[x][y] = Cells[x][y].copy ();
            }
        }
    }

    double t_h = 255.0 / 2;

    void initialiseImage() {
        try {
            image = ImageIO.read (new File (Utilities.img_location));
        } catch (IOException e) {
            e.printStackTrace ();
        }
        double img_width = image.getWidth (), img_height = image.getHeight ();
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                int img_x = (int) (x * (img_width / GRID_WIDTH)),
                        img_y = (int) (y * (img_height / GRID_HEIGHT));
                Color c = new Color (image.getRGB (img_x, img_y));

                boolean above_thresh = c.getRed () > t_h || c.getGreen () > t_h || c.getBlue () > t_h;
                boolean alive = false;

                if (above_thresh) {
                    if (isEdge (img_x, img_y)) {
                        alive = true;
                    }
                }

                Cells[x][y] = new Cell (alive);
                Buffer[x][y] = Cells[x][y].copy ();
            }
        }
    }

    boolean isEdge(int x, int y) {
        int right = (x + 1) % (image.getWidth () - 1);
        int left = (x - 1) < 0 ? image.getWidth () - 1 : x - 1;
        int down = (y + 1) % (image.getHeight () - 1);
        int up = (y - 1) < 0 ? image.getHeight () - 1 : y - 1;

        if (isBelowThreshold (left, up) || isBelowThreshold (right, up) ||
                isBelowThreshold (x, up) || isBelowThreshold (x, down) ||
                isBelowThreshold (left, down) || isBelowThreshold (right, down) ||
                isBelowThreshold (right, y) || isBelowThreshold (left, y))
            return true;

        return false;
    }

    boolean isBelowThreshold(int x, int y) {
        boolean value;
        Color c = new Color (image.getRGB (x, y));
        value = c.getRed () < t_h || c.getGreen () < t_h || c.getBlue () < t_h;
        return value;
    }

    public static void main(String[] args) {
        Main ob = new Main ();
        ob.start ();
    }

}
