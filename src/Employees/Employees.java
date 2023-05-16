package Employees;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public abstract class Employees implements IBehaviour {

    public static final int IMAGE_WIDTH = 96;
    public static final int IMAGE_HEIGHT = 64;
    public String defaultPath = "src/Images/manager.png";

    private final long createTime;
    private long lifeTime;
    private final String id;
    private int x;
    private int y;
    public JLabel LayObject;

    Employees() {
      this(0, 0);
    }

    Employees(String id, int x, int y, long createTime, long lifeTime) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.createTime = createTime;
        setLifetime(lifeTime);
    }

    Employees(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = generateID();
        this.createTime = System.currentTimeMillis();
        setImage();
        setPosition(x,y);
    }

    public void setImage() {
        final ImageIcon tmp = changeImageSize(defaultPath);
        this.LayObject = new JLabel(tmp);
    }

    public long getLifetime() {
        this.lifeTime = System.currentTimeMillis() - this.createTime;
        return this.lifeTime;
    }

    public void setLifetime(long lifeTime) {
        this.lifeTime = lifeTime;
    }

    public String getId() {
        return id;
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

        var image = new ImageIcon(dimg);
        return image;
    }
}
