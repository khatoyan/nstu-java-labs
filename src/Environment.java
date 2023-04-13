import Employees.Developer;
import Employees.Employees;
import Employees.Manager;
import Employees.CounterLabel;
import ui_components.ButtonsPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class Environment
{
    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 800;
    private static final  Color BACK_COLOR = Color.WHITE;
    private static final  Color INFO_COLOR = new Color(255, 255, 255);

    private int generatePeriod = 3;
    private int chanceOfBirth = 8;

    private static JFrame frame;
    private final JLabel simTimeLabel;
    private final JLabel simulationInfo;
    private final JPanel dataPanel;
    private final JPanel mainPanel;
    private JPanel objectsPanel;
    private final JButton startButton;
    private final JButton stopButton;
    private final JRadioButton showInfoButton;
    private final JRadioButton NotShowInfoButton;
    private final ButtonGroup switchButtons;
    private final JTextField inputText;
    private final JCheckBox checkBox;
    private final JPanel buttonsPanel = new JPanel();
    private Timer devTimer;
    private Timer manTimer;
    private final ArrayList<Employees> emplList = new ArrayList<Employees>();
    private final long simStartTime;
    private int simTime;
    private boolean isAllowed = true;
    private boolean showInfo = false;


    Environment()
    {
        frame = new JFrame("Lab 1");
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
        frame.setFocusable(true);

        simTimeLabel = new JLabel();
        simulationInfo = new JLabel();
        dataPanel = new JPanel();
        mainPanel = new JPanel();
        objectsPanel = new JPanel(null);
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        NotShowInfoButton = new JRadioButton("Убрать текст", false);
        showInfoButton = new JRadioButton("Показать текст", true);
        switchButtons = new ButtonGroup();

        switchButtons.add(showInfoButton);
        switchButtons.add(NotShowInfoButton);
        inputText = new JTextField( 30);

        JLabel myLabel = new JLabel("Введите время рождения в секундах:");
        JPanel pan = new JPanel(new BorderLayout());
        pan.add(myLabel);
        pan.add(inputText);
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        checkBox = new JCheckBox("Показывать информацию");
        handleInputChange();

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        simTimeLabel.setSize(800, 60);

        simStartTime = System.currentTimeMillis();

        buttonsPanel.setSize(800, 100);
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(pan);
        buttonsPanel.add(checkBox);
        buttonsPanel.add(showInfoButton);
        buttonsPanel.add(NotShowInfoButton);

        mainPanel.add(buttonsPanel);
        mainPanel.add(objectsPanel);
        mainPanel.add(simTimeLabel);

        frame.getContentPane().add(mainPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible( true );
        frame.repaint();
        //handleKeyActions();
        start();
    }

    void handleInputChange() {
        inputText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    generatePeriod = Integer.parseInt(inputText.getText());
                } catch (NumberFormatException ex) {
                    generatePeriod = 1;
                    inputText.setText("1");
                    JOptionPane.showMessageDialog(frame,"Ошибка! Некорректный ввод. Подставлено значение 1");
                }
            }
        });

        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInfo = !showInfo;
            }
        });
    }

    void start() {
        settingKeys();
        changeSimTime();
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

    public int create(long time, int objType) {

        int rand = (int) Math.ceil(Math.random() * 8);
        int rand2 = (int) Math.ceil(Math.random() * 8);

        if (rand < chanceOfBirth && objType == 1) {
            if (rand*100 + Manager.IMAGE_WIDTH > SCREEN_WIDTH) {
                rand = SCREEN_WIDTH - Manager.IMAGE_WIDTH;
            }
            var newObj = new Developer(rand*100, rand2*100);
            emplList.add(newObj);
            objectsPanel.add(newObj.LayObject);
            objectsPanel.repaint();
            frame.repaint();
        }

        if (rand2 < generatePeriod && objType == 2) {
            if (rand2*100 + Manager.IMAGE_HEIGHT > objectsPanel.getHeight()) {
                rand2 = objectsPanel.getHeight() - Manager.IMAGE_HEIGHT;
            }

            var newObj = new Manager(rand*1, rand2*1);
            emplList.add(newObj);
            objectsPanel.add(newObj.LayObject);
            objectsPanel.repaint();
            frame.repaint();
        }

        if (emplList.size() > 50) isAllowed = false;

        return 2;
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
                create(simStartTime, 2);
            }
        };

        devTimer = new Timer(generatePeriod*1000, action);
        manTimer = new Timer(generatePeriod*1000, manAction);
        devTimer.start();
        manTimer.start();
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
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

    JDialog createModal() {
        final var modal = new JDialog(frame, "Завершить?", true);
        final var textArea = new JTextArea(5, 20);

        modal.setSize(500,300);

        textArea.setEditable(false);
        textArea.append(
                "Время симуляции: " + simTime + " " +
                "Кол-во менеджеров: " + Manager.countMan + " " +
                "Кол-во разработчиков: " + Developer.countDev
        );

        textArea.setBounds(1, 1, 500, 100);
        modal.getContentPane().add(textArea);
        modal.setLocationRelativeTo(null);

        return modal;
    }

    void handleStopActions() {
        if (showInfo) {
            final var modal = createModal();
            final var modalOKButton = new JButton("Ок");
            final var modalCancelButton = new JButton("Отмена");

            Container pane = modal.getContentPane();
            pane.setLayout(null);

            modalOKButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onStop();
                    modal.dispose();
                }
            });

            modalCancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    modal.dispose();
                }
            });
            modalOKButton.setBounds(100, 130, 80, 30);
            modalCancelButton.setBounds(300, 130, 80, 30);
            System.out.println("in");
            pane.add(modalOKButton);
            pane.add(modalCancelButton);
            modal.setVisible(true);

            return;
        }
        onStop();
    }

    void onStop() {
        isAllowed = false;
        emplList.clear();
        frame.getContentPane().removeAll();
        frame.repaint();
        createDataText();
        dataPanel.setVisible(!dataPanel.isVisible());
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
    }

    void onStart() {
        generate();
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
    }

    void onShow() {
        if (simTimeLabel.isVisible()) {
            return;
        }

        simTimeLabel.setVisible(!simTimeLabel.isVisible());
    }

    void onHide() {
        if (!simTimeLabel.isVisible()) {
            return;
        }

        simTimeLabel.setVisible(!simTimeLabel.isVisible());
    }
    private void settingKeys() {
        KeyEventDispatcher dispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    System.out.println("kkk");
                    if (e.getKeyCode() == KeyEvent.VK_E) {
                        handleStopActions();
                    }

                    if (e.getKeyCode() == KeyEvent.VK_T) {
                        onShow();
                    }

                    else if (e.getKeyCode() == KeyEvent.VK_B) {
                        onStart();
                    }
                }
                return false;
            }
        };

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(dispatcher);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStart();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleStopActions();
            }
        });

        showInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onShow();
            }
        });

        NotShowInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHide();
            }
        });
    }

    public static void main(String[] args)
    {
        var beg = new Environment();
    }
}
