import Employees.Developer;
import Employees.Employees;
import Employees.Manager;
import Employees.CounterLabel;

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
import java.util.HashMap;
import java.util.TreeSet;

public class Environment
{
    /* Константы */

    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 800;
    private static final  Color BUTTON_COLOR =  new Color(50, 205, 50);
    private static final  Color BACK_COLOR =  new Color(64, 181, 173);
    private static final  Color HEADER_COLOR =  new Color(135, 206, 235);
    private static final  Color STOP_COLOR =  new Color(255, 79, 91);
    private static final  Color INFO_COLOR = new Color(159, 226, 191);
    private final long simStartTime;

    /* Изменяемые переменные */

    private int generatePeriod = 1;
    private int chanceOfBirth = 8;
    private int simTime;
    private boolean isAllowed = true;
    private boolean showInfo = false;

    /* Таймеры */

    private Timer devTimer;
    private Timer manTimer;

    /* Коллекции данных */

    private final ArrayList<Employees> emplList = new ArrayList<>();
    private final TreeSet IdList = new TreeSet();
    private final HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

    /* Блок фрейма и панелей */

    private static JFrame frame;
    private final JLabel simTimeLabel;
    private final JLabel simulationInfo;
    private final JPanel dataPanel;
    private final JPanel mainPanel;
    private final JPanel devLifeTimeData;
    private final JPanel manLifeTimeData;
    private JPanel objectsPanel;
    private final JPanel buttonsPanel = new JPanel();

    /* Блок кнопок и чекбоксов */

    private final JButton startButton;
    private final JButton stopButton;
    private final JRadioButton showInfoButton;
    private final JRadioButton NotShowInfoButton;
    private final ButtonGroup switchButtons;
    private final JCheckBox checkBox;
    private final JTextField inputText;
    private final JTextField devLifeTime;
    private final JTextField manLifeTime;

    /* Блок с меню */

    private final JMenuBar menuBar;
    private final JMenu fileMenu;
    private final JMenu newMenu;
    private final JMenuItem showTextItem;
    private final JMenuItem hideTextItem;
    private final JMenuItem startItem;
    private final JMenuItem stopItem;
    private final JMenuItem exitItem;

    Environment()
    {
        frame = new JFrame("Lab 1");
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
        frame.setFocusable(true);

        dataPanel = new JPanel();
        mainPanel = new JPanel();
        devLifeTimeData = new JPanel(new BorderLayout());
        manLifeTimeData = new JPanel(new BorderLayout());
        objectsPanel = new JPanel(null);
        objectsPanel.setPreferredSize(new Dimension(SCREEN_WIDTH, 500));
        JPanel pan = new JPanel(new BorderLayout());

        simTimeLabel = new JLabel();
        simulationInfo = new JLabel();
        JLabel myLabel = new JLabel("Введите время рождения в секундах:");
        JLabel devLifeTimeLabel = new JLabel("Время жизни разрабов");
        JLabel manLifeTimeLabel = new JLabel("Время жизни менеджеров");

        Font font = new Font("Verdana", Font.PLAIN, 11);

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");

        NotShowInfoButton = new JRadioButton("Убрать текст", false);
        showInfoButton = new JRadioButton("Показать текст", true);
        switchButtons = new ButtonGroup();
        checkBox = new JCheckBox("Показывать информацию");
        inputText = new JTextField( 25);
        devLifeTime = new JTextField( 25);
        manLifeTime = new JTextField( 25);

        NotShowInfoButton.setBackground(HEADER_COLOR);
        showInfoButton.setBackground(HEADER_COLOR);

        switchButtons.add(showInfoButton);
        switchButtons.add(NotShowInfoButton);

        devLifeTimeData.add(devLifeTimeLabel);
        devLifeTimeData.add(devLifeTime);
        manLifeTimeData.add(manLifeTimeLabel);
        manLifeTimeData.add(manLifeTime);
        devLifeTimeData.setLayout(new BoxLayout(devLifeTimeData, BoxLayout.Y_AXIS));
        manLifeTimeData.setLayout(new BoxLayout(manLifeTimeData, BoxLayout.Y_AXIS));

        pan.add(myLabel);
        pan.add(inputText);
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        pan.setBackground(HEADER_COLOR);

        simTimeLabel.setSize(800, 60);
        simStartTime = System.currentTimeMillis();

        /* Блок кнопок */

        buttonsPanel.setSize(800, 100);
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(checkBox);
        buttonsPanel.add(showInfoButton);
        buttonsPanel.add(NotShowInfoButton);
        buttonsPanel.add(pan);
        buttonsPanel.add(devLifeTimeData);
        buttonsPanel.add(manLifeTimeData);

        /* Главная панель */

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(buttonsPanel);
        mainPanel.add(objectsPanel);
        mainPanel.add(simTimeLabel);

        /* Меню */

        menuBar = new JMenuBar();
        fileMenu = new JMenu("Настройки");
        fileMenu.setFont(font);

        newMenu = new JMenu("Текст");
        newMenu.setFont(font);
        fileMenu.add(newMenu);

        showTextItem = new JMenuItem("Показать");
        showTextItem.setFont(font);
        newMenu.add(showTextItem);

        hideTextItem = new JMenuItem("Убрать");
        hideTextItem.setFont(font);
        newMenu.add(hideTextItem);

        startItem = new JMenuItem("Старт");
        startItem.setFont(font);
        fileMenu.add(startItem);

        stopItem = new JMenuItem("Стоп");
        stopItem.setFont(font);
        fileMenu.add(stopItem);

        fileMenu.addSeparator();

        exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        handleInputChange();
        handleMenuButtons();

        startButton.setBackground(BUTTON_COLOR);
        stopButton.setBackground(STOP_COLOR);
        objectsPanel.setBackground(BACK_COLOR);
        buttonsPanel.setBackground(HEADER_COLOR);
        devLifeTimeData.setBackground(HEADER_COLOR);
        manLifeTimeData.setBackground(HEADER_COLOR);
        simTimeLabel.setBackground(BACK_COLOR);

        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(mainPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible( true );
        frame.repaint();
        start();
    }

    void handleMenuButtons() {
        startItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStart();
            }
        });

        stopItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleStopActions();
            }
        });

        showTextItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onShow();
            }
        });

        hideTextItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHide();
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    void handleInputChange() {
        inputText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    generatePeriod = Integer.parseInt(inputText.getText());
                    devTimer.setDelay(generatePeriod*1000);
                    manTimer.setDelay(generatePeriod*1000);
                    inputText.setText("");
                    System.out.println(generatePeriod);
                } catch (NumberFormatException ex) {
                    generatePeriod = 1;
                    devTimer.setDelay(generatePeriod*1000);
                    manTimer.setDelay(generatePeriod*1000);
                    inputText.setText("1");
                    JOptionPane.showMessageDialog(frame,"Ошибка! Некорректный ввод. Подставлено значение 1");
                }
            }

        });

        devLifeTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Developer.devLifeTime = Integer.parseInt(devLifeTime.getText());
                    devLifeTime.setText("");
                } catch (NumberFormatException ex) {
                    Developer.devLifeTime = 15;
                    devLifeTime.setText("15");
                    JOptionPane.showMessageDialog(frame,"Ошибка! Некорректный ввод. Подставлено значение 15");
                }
            }
        });
        manLifeTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Manager.manLifeTime = Integer.parseInt(manLifeTime.getText());
                    manLifeTime.setText("");
                } catch (NumberFormatException ex) {
                    Manager.manLifeTime = 15;
                    manLifeTime.setText("15");
                    JOptionPane.showMessageDialog(frame,"Ошибка! Некорректный ввод. Подставлено значение 15");
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
        clearTimer();
    }

    void deleteDeadObjects(long currTime) {

        for (int i = 0; i < emplList.size(); i++) {
            Employees obj = emplList.get(i);

            if (obj.LayObject != null) {
                if (currTime - obj.getCreateTime() >= Developer.devLifeTime*1000 && obj instanceof Developer) {
                    objectsPanel.remove(obj.LayObject);
                    emplList.remove(obj);
                }
                if (currTime - obj.getCreateTime() >= Manager.manLifeTime*1000 && obj instanceof Manager) {
                    objectsPanel.remove(obj.LayObject);
                    emplList.remove(obj);
                }
            }

        }
    }

    Timer simulationTimer;
    public void changeSimTime() {

        simulationTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                long tmp = System.currentTimeMillis();
                long dif = (tmp - simStartTime) / 1000;
                simTime = (int)dif;
                simTimeLabel.setText("Время симуляции: " + dif);
                frame.repaint();
            }
        });

        simulationTimer.start();
    }

    public void clearTimer() {

        Timer clearTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteDeadObjects(System.currentTimeMillis());
                frame.repaint();
            }
        });

        clearTimer.start();
    }

    public int create(long time, int objType) {

        if (!isAllowed) {
            return -1;
        }

        int rand = (int) Math.ceil(Math.random() * 8);
        int rand2 = (int) Math.ceil(Math.random() * 8);

        if (rand < chanceOfBirth && objType == 1) {
            var newObj = new Developer(
                    getRandomNumber(0, objectsPanel.getWidth() - Employees.IMAGE_WIDTH),
                    getRandomNumber(0,  objectsPanel.getHeight() - Employees.IMAGE_HEIGHT ));
            emplList.add(newObj);
            IdList.add(newObj.getId());
            hashMap.put(newObj.getId(), (int)newObj.getCreateTime());
            objectsPanel.add(newObj.LayObject);
            frame.repaint();
        }

        if (rand2 < chanceOfBirth && objType == 2) {

            if (!isAllowed) {
                return -1;
            }

            var newObj = new Manager(
                    getRandomNumber(0, objectsPanel.getWidth() - Employees.IMAGE_WIDTH),
                    getRandomNumber(0,  objectsPanel.getHeight() - Employees.IMAGE_HEIGHT ));

            emplList.add(newObj);
            IdList.add(newObj.getId());
            hashMap.put(newObj.getId(), (int)newObj.getCreateTime());
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
                if (!isAllowed) return;
                create(simStartTime, 2);
            }
        };

        devTimer = new Timer((generatePeriod*1000), action);
        manTimer = new Timer((generatePeriod*1000), manAction);
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
        mainPanel.add(dataPanel);
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
            pane.add(modalOKButton);
            pane.add(modalCancelButton);
            modal.setVisible(true);

            return;
        }
        onStop();
    }

    void onStop() {
        isAllowed = false;
        simulationTimer.stop();
        emplList.clear();
        hashMap.clear();
        IdList.clear();
        objectsPanel.removeAll();
        frame.repaint();
        createDataText();
        dataPanel.setVisible(!dataPanel.isVisible());
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
    }

    void onStart() {
        dataPanel.setVisible(!dataPanel.isVisible());
        isAllowed = true;
        simulationTimer.start();
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
                    if (e.getKeyCode() == KeyEvent.VK_E) {
                        handleStopActions();
                    }

                    if (e.getKeyCode() == KeyEvent.VK_T) {
                        simTimeLabel.setVisible(!simTimeLabel.isVisible());
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
