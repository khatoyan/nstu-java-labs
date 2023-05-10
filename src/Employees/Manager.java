package Employees;

import javax.swing.*;

public class Manager extends Employees {

    public static int manLifeTime = 10;
    public static int countMan = 0;

    private final static String PATH = "src/Images/manager.png"; //Путь к изображению объекта
    private ImageIcon imageObject = new ImageIcon(PATH); //Изобраение объекта

    @Override
    public void setImage() {
        final ImageIcon tmp = changeImageSize(PATH);
        this.LayObject = new JLabel(tmp);
    }

    public Manager() {
        this(0, 0);
    }

    public Manager(int x, int y) {
        super(x, y);
        countMan++;
        setImage();
    }
}

