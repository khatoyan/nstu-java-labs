package Employees;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Employees {

    protected static int IMAGE_WIDTH = 120;
    protected static int IMAGE_HEIGHT = 80;

    private int x;
    private int y;

    Employees() {
        this.x = 0;
        this.y = 0;
    }

    Employees(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected ImageIcon changeImageSize(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image dimg = img.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);

        var tmp = new ImageIcon(dimg);
        return tmp;
    }
}
