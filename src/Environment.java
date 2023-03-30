import Employees.Developer;
import Employees.Employees;
import Employees.Manager;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

public class Environment
{
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 800;
    //private static final  Color BACK_COLOR = new Color(229, 209, 192);
    private static final  Color BACK_COLOR = Color.WHITE;
    private static final  Color INFO_COLOR = new Color(255, 255, 255);

    private static JFrame frame;
    private final JLabel simTimeLabel;
    private final JLabel simulationInfo;
    private final JPanel dataPanel;
    private Timer devTimer;
    private Timer manTimer;
    private final ArrayList<Employees> emplList = new ArrayList<Employees>();
    private final long simStartTime;
    private int simTime;
    private boolean isAllowed = true;


    Environment()
    {
        frame = new JFrame("Lab 1");
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
        frame.setLocationRelativeTo(null);
        frame.setVisible( true );
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(BACK_COLOR);

        simTimeLabel = new JLabel();
        simulationInfo = new JLabel();
        dataPanel = new JPanel();
        simTimeLabel.setBounds(5, 5, 200, 50);
        simStartTime = System.currentTimeMillis();

        frame.add(simTimeLabel);
        start();
    }

    void start() {
        changeSimTime();
        handleKeyActions();
    }

    public void changeSimTime() {

        Timer simulationTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                long dif = (System.currentTimeMillis() - simStartTime) / 1000;
                simTime = (int)dif;
                simTimeLabel.setText("Время симуляции: " + dif);
                frame.repaint();
            }
        });

        simulationTimer.start();
    }

    public void create(long time, int objType) {


        int rand = (int) Math.ceil(Math.random() * 8);
        int rand2 = (int) Math.ceil(Math.random() * 8);

        if (rand < 7 && objType == 1) {
            var newObj = new Developer(rand*100, rand2*100);
            emplList.add(newObj);
            frame.add(newObj.LayObject);
            frame.repaint();
        }

        if (rand < 6 && objType == 2) {
            var newObj = new Manager(rand*100, rand2*100);
            emplList.add(newObj);
            frame.add(newObj.LayObject);
            frame.repaint();
        }

        if (emplList.size() > 50) isAllowed = false;
    }

    void generate() {
        var action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isAllowed) return;
                create(simStartTime, 1);
            }
        };

        var manAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isAllowed) return;
                create(simStartTime, 2);
            }
        };

        devTimer = new Timer(1200, action);
        manTimer = new Timer(2000, manAction);
        devTimer.start();
        manTimer.start();
    }

    void createDataText() {
        dataPanel.add(simulationInfo);
        dataPanel.setBounds(200, 400, 600, 50);
        dataPanel.setBackground(INFO_COLOR);
        dataPanel.setVisible(false);
        dataPanel.setBorder(BorderFactory.createMatteBorder(
                2, 2, 2, 2, Color.black));
        frame.add(dataPanel);
        addData();
    }

    void addData() {
        simulationInfo.setText(
            "Время симуляции: " + simTime + " " +
            "Кол-во менеджеров: " + Manager.countMan + " " +
            "Кол-во разработчиков: " + Developer.countDev
        );
        frame.repaint();
    }

    void handleKeyActions() {
        createDataText();
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    isAllowed = false;
                    emplList.clear();
                    frame.getContentPane().removeAll();
                    frame.repaint();
                    createDataText();
                    dataPanel.setVisible(!dataPanel.isVisible());
                }

                if (e.getKeyCode() == KeyEvent.VK_T) {
                    simTimeLabel.setVisible(!simTimeLabel.isVisible());
                }

                else if (e.getKeyCode() == KeyEvent.VK_B) {
                    generate();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });

    }

    public static void main(String[] args)
    {
        var beg = new Environment();
    }
}

