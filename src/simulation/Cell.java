package simulation;

import java.awt.Color;

public class Cell {
    static Color[] colors = new Color[]{
//            Color.WHITE,
//            Color.decode ("#711c91"),
//            Color.decode ("#ea00d9"),
//            Color.decode ("#5c00ff"),
//            Color.decode ("#0abdc6"),
//            Color.decode ("#133e7c"),
//            Color.decode ("#091833"),
//            Color.decode ("#000103"),

//            Color.WHITE,
//            Color.decode ("#cc2b5e"),
//            Color.decode ("#f59c00"),
//            Color.decode ("#b36a46"),
//            Color.decode ("#753a88"),
//            Color.decode ("#3b1d44"),
//            Color.decode ("#86377f"),
//            Color.decode ("#000103"),
            Color.WHITE,
//            Color.decode("#fec195"),
//            Color.decode("#f78c8b"),
//            Color.decode("#5b6ee4"),
//            Color.decode("#120d6c"),
//            Color.decode("#0d0743"),
//            Color.WHITE,
            Color.WHITE,
            Color.decode("#f7b06a"),
            Color.decode("#f3a1ea"),
            Color.decode("#5f52a1"),
            Color.decode("#161b73"),
    Color.BLACK,
    };
    static float[] steps = new float[] {10F,20F,30F,20F,50F, 500F};//,5F,15F,25F,29F,50F};//{1F, 5F, 10F, 30F, 80F, 120F, 500F};
    static float[] red = new float[colors.length];
    static float[] green = new float[colors.length];
    static float[] blue = new float[colors.length];

    static {
        for (int i = 0; i < colors.length; i++) {
            red[i] = colors[i].getRed () / 255.0F;
            green[i] = colors[i].getGreen () / 255.0F;
            blue[i] = colors[i].getBlue () / 255.0F;
        }
    }

    boolean alive = false;

    float r, g, b;
    int color_index = 0;
    Slope Red = new Slope (red[0], red[1], steps[0]);
    Slope Green = new Slope (green[0], green[1], steps[0]);
    Slope Blue = new Slope (blue[0], blue[1], steps[0]);

    void AdvanceColors() {
        if (Red.advanceSlope ()) {
            Green.advanceSlope ();
            Blue.advanceSlope ();
            r = Red.current_value;
            g = Green.current_value;
            b = Blue.current_value;
        } else if (color_index < steps.length - 1) {
            ResetColors (color_index + 1, color_index + 2,steps[color_index + 1]);
        }
    }

    void ResetColors() {
        ResetColors (0, 1,steps[0]);
    }

    void ResetColors(int color_index, int color_index2,float steps) {
        this.color_index = color_index;
        Red.createSlope (red[color_index], red[color_index2], steps);
        Green.createSlope (green[color_index], green[color_index2], steps);
        Blue.createSlope (blue[color_index], blue[color_index2], steps);

        r = Red.current_value;
        g = Green.current_value;
        b = Blue.current_value;
    }

    Cell(boolean alive) {
        this.alive = alive;
        if (alive) {
            color_index = 0;
            r = Red.current_value;
            g = Green.current_value;
            b = Blue.current_value;
        } else {
            color_index = steps.length - 1;
            ResetColors (steps.length - 2, steps.length - 1,1);
        }
    }

    Cell(boolean alive, Slope Red, Slope Green, Slope Blue, int color_index) {
        this.alive = alive;
        this.Red = Red;
        this.Green = Green;
        this.Blue = Blue;
        this.color_index = color_index;
    }

    Cell copy() {
        return new Cell (alive, Red, Green, Blue, color_index);
    }

    void copyInformation(Cell cell) {
        this.alive = cell.alive;

        this.Red = cell.Red;
        this.Green = cell.Green;
        this.Blue = cell.Blue;
        this.r = cell.r;
        this.g = cell.g;
        this.b = cell.b;
        this.color_index = cell.color_index;
    }
}
