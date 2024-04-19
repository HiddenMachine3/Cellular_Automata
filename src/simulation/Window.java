package simulation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Window {

    Cell[][] Cells;

    int GRID_WIDTH, GRID_HEIGHT;
    double CELL_SIZE;
    BufferedImage screen;

    Window(Cell[][] Cells, int GRID_WIDTH, int GRID_HEIGHT) {
        this.Cells = Cells;
        this.GRID_WIDTH = GRID_WIDTH;
        this.GRID_HEIGHT = GRID_HEIGHT;
        CELL_SIZE = 690.0 / GRID_HEIGHT;
        screen = new BufferedImage (GRID_WIDTH, GRID_HEIGHT, BufferedImage.TYPE_INT_RGB);

        initialiseComponents ();
    }

    void window() {
        frame.setExtendedState (JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo (null);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        frame.setContentPane (panel1);

        frame.setVisible (true);
    }

    public void setCells(Cell[][] cells) {
        Cells = cells;
    }

    class Canvas extends JComponent {
        public void paint(Graphics gg) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                for (int y = 0; y < GRID_HEIGHT; y++) {
                    Cell cell = Cells[x][y];
                    screen.setRGB (x, y, new Color (cell.r, cell.g, cell.b).getRGB ());
                }
            }
            gg.drawImage (screen, 0, 0, 1296, 690, null);

            if (Utilities.saveImage) {
                Utilities.saveImage (screen);
            }

            for (int i = 0; i < this.getComponentCount (); i++) {
                this.getComponent (i).repaint ();
            }
        }
    }


    private Canvas canvas;
    public JPanel panel1;
    private JSlider slider1;
    private JPanel panel2;
    private JButton Save_button;
    private JButton Open_button;
    public JFrame frame = new JFrame ();

    void createUIComponents() {
        canvas = new Canvas ();
    }

    void initialiseComponents() {
        panel2.setBackground (new Color (0, 0, 0, 0));
        slider1.addChangeListener (new ChangeListener () {
            @Override
            public void stateChanged(ChangeEvent e) {
                Utilities.timestep = slider1.getMaximum () - slider1.getValue ();
            }
        });
        slider1.setValue ((int) (slider1.getMaximum () - Utilities.timestep));
        slider1.setMinimum (1000000);
        slider1.setMaximum (1000000000);
        slider1.setUI (new customSlider (slider1));
        slider1.setOpaque (false);
        Save_button.setBackground (new Color (0, 0, 0, 0));
        Open_button.setBackground (new Color (0, 0, 0, 0));
        Save_button.setBorder (null);
        Open_button.setBorder (null);
        Save_button.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utilities.setSaveImage (true);
            }
        });
        Open_button.addActionListener (new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utilities.setLoadImage (true);
            }
        });
        try {
            BufferedImage save_img = Utilities.resize (
                    ImageIO.read (new File ("icons\\save_icon.png"))
                    , 40, 40);
            Save_button.setIcon (new ImageIcon (save_img));
            BufferedImage open_img = Utilities.resize (
                    ImageIO.read (new File ("icons\\open.png"))
                    , 50, 50);
            Open_button.setIcon (new ImageIcon (open_img));
        } catch (IOException e) {
            e.printStackTrace ();
        }

        window ();
    }

    class customSlider extends BasicSliderUI {

        public customSlider(JSlider slider) {
            super (slider);
        }

        @Override
        public void paintThumb(Graphics gg) {
            Graphics2D g = (Graphics2D) gg;
            g.setColor (Color.decode ("#a705ff"));
            Rectangle t = thumbRect;
            g.fill (t);
        }

        @Override
        public void paintTrack(Graphics gg) {
            Graphics2D g = (Graphics2D) gg;
            g.setColor (Color.decode ("#000034"));
            Rectangle2D t = new Rectangle2D.Double (trackRect.x, trackRect.y + 5 + 2.5, trackRect.width, 5);
            g.fill (t);
        }

        @Override
        public void paintFocus(Graphics g) {
            //super.paintFocus (g);
        }
    }

}
