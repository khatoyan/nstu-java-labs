package Employees;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public abstract class Employees {

    public static final int IMAGE_WIDTH = 96;
    public static final int IMAGE_HEIGHT = 64;

    protected long createTime;
    protected long lifeTime;
    private String id;
    private int x;
    private int y;
    public JLabel LayObject = null; //Слой объе кта который ложится на экран

    public long tmp;
    Employees() {
      this(0, 0);
    }

    Employees(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = generateID();
        this.createTime = System.currentTimeMillis();
        lifeTime = 2;
    }

    public long getLifetime() {
        return System.currentTimeMillis() - this.createTime;
    }

    public String getId() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    protected String generateID() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString().replaceAll("-", "").substring(0, 4);
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        LayObject.setBounds(x,y,100,100);
        LayObject.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
    }
}
