package Employees;
import javax.swing.*;

public class Developer extends Employees implements IBehaviour {

    public static int devLifeTime = 10;
    public static int countDev = 0;

    private final static String PATH = "src/Images/developer.png"; //Путь к изображению объекта
    private ImageIcon imageObject = new ImageIcon(PATH); //Изобраение объекта

    @Override
    public void setImage() {
        final ImageIcon tmp = changeImageSize(PATH);
        this.LayObject = new JLabel(tmp);
    }

    public Developer(String id, int x, int y, long createTime, long lifeTime) {
        super(id, x, y, createTime, lifeTime);
        setImage();
        setPosition(x,y);
    }

    public Developer() {
        this(0, 0);
    }

    public Developer(int x, int y) {
        super(x, y);
        countDev++;
        setImage();
    }
}

