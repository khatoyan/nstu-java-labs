package Employees;
import javax.swing.*;

public class Developer extends Employees implements IBehaviour {

    public static int devLifeTime = 10;
    public static int countDev = 0;
    private int x;
    private int y;

    private final static String PATH = "src/Images/developer.png"; //Путь к изображению объекта
    private ImageIcon imageObject = new ImageIcon(PATH); //Изобраение объекта

    public Developer() {
        super();
        countDev++;
        imageObject = changeImageSize(PATH);
        this.LayObject = new JLabel(imageObject);
        setPosition(x,y);
    }

    public Developer(int x, int y) {
        super(x, y);
        countDev++;
        imageObject = changeImageSize(PATH);
        this.LayObject = new JLabel(imageObject);
        setPosition(x, y);
    }

    public Developer(String id, int x, int y, long createTime, long lifeTime) {
        this(x, y);
        setID(id);
        this.lifeTime = lifeTime;
        this.createTime = createTime;
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

