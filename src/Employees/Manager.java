package Employees;

import javax.swing.*;

public class Manager extends Employees implements IBehaviour {

    public static int manLifeTime = 10;
    public static int countMan = 0;
    private int x;
    private int y;

    private final static String PATH = "src/Images/manager.png"; //Путь к изображению объекта
    private ImageIcon imageObject = new ImageIcon(PATH); //Изобраение объекта


    public Manager() {
        super();
        countMan++;
        imageObject = changeImageSize(PATH);
        this.LayObject = new JLabel(imageObject);
        setPosition(x,y);
    }

    public Manager(int x, int y) {
        super(x, y);
        countMan++;
        imageObject = changeImageSize(PATH);
        this.LayObject = new JLabel(imageObject);
        setPosition(x, y);
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

