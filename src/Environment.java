import AI.DevAI;
import AI.ManAI;
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
import javax.swing.plaf.ColorUIResource;
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

    /* Цвета */

    private static Color BUTTON_COLOR =  new Color(50, 205, 50);
    private static Color BACK_COLOR =  new Color(64, 181, 173);
    private static Color HEADER_COLOR =  new Color(135, 206, 235);
    private static Color STOP_COLOR =  new Color(255, 79, 91);
    private static Color GOOD_COLOR =  new Color(83, 128, 213);
    private static Color INFO_COLOR = new Color(159, 226, 191);
    private static Color OBJ_COLOR = new Color(240, 230, 140);
    private static Color LIGHT_COLOR = new Color(255,255,255);

    /* Изменяемые переменные */

    private final long simStartTime;
    private int generatePeriod = 5;
    private int chanceOfBirth = 8;
    private int simTime;
    private boolean isLightTheme = true;
    private boolean isAllowed = true;
    private boolean showInfo = false;

    /* Таймеры */

    private Timer devTimer;
    private Timer manTimer;
    private Timer simulationTimer;

    /* Коллекции данных */

    private ArrayList<Employees> emplList = new ArrayList<>();
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
    private final JPanel pan;
    private JPanel objectsPanel;
    private final JPanel buttonsPanel = new JPanel();

    /* Блок кнопок и чекбоксов */

    private final JButton startButton;
    private final JButton stopButton;
    private final JButton showObjectsButton;
    private final JButton changeThemeButton;
    private final JButton saveDBButton;
    private final JButton loadDBButton;

    private final ButtonGroup switchButtons;
    private final JCheckBox checkBox;
    private final JTextField inputText;
    private final JTextField devLifeTime;
    private final JTextField manLifeTime;

    /* Блок с меню */

    private final JMenuBar menuBar;
    private final JMenu fileMenu;
    private final JMenu newMenu;
    private final JMenu nMenu;
    private final JMenuItem showTextItem;
    private final JMenuItem hideTextItem;
    private final JMenuItem onDevAI;
    private final JMenuItem offDevAI;
    private final JMenuItem onManAI;
    private final JMenuItem offManAI;
    private final JMenuItem startItem;
    private final JMenuItem stopItem;
    private final JMenuItem exitItem;

    private DevAI devAI;
    private ManAI manAI;

    private final dbMethods dbMethods;

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
        pan = new JPanel(new BorderLayout());

        simTimeLabel = new JLabel();
        simulationInfo = new JLabel();
        JLabel myLabel = new JLabel("Введите время рождения в секундах:");
        JLabel devLifeTimeLabel = new JLabel("Время жизни разрабов");
        JLabel manLifeTimeLabel = new JLabel("Время жизни менеджеров");

        myLabel.setForeground(LIGHT_COLOR);
        devLifeTimeLabel.setForeground(LIGHT_COLOR);
        manLifeTimeLabel.setForeground(LIGHT_COLOR);

        Font font = new Font("Verdana", Font.PLAIN, 11);

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        showObjectsButton = new JButton("Текущие объекты");
        changeThemeButton = new JButton("Сменить тему");
        saveDBButton = new JButton("Сохранить в БД");
        loadDBButton = new JButton("Загрузить из БД");

        switchButtons = new ButtonGroup();
        checkBox = new JCheckBox("Показывать информацию");
        inputText = new JTextField( 25);
        devLifeTime = new JTextField( 25);
        manLifeTime = new JTextField( 25);

        devLifeTimeData.add(devLifeTimeLabel);
        devLifeTimeData.add(devLifeTime);
        devLifeTimeData.setBackground(LIGHT_COLOR);
        manLifeTimeData.add(manLifeTimeLabel);
        manLifeTimeData.add(manLifeTime);
        devLifeTimeData.setLayout(new BoxLayout(devLifeTimeData, BoxLayout.Y_AXIS));
        manLifeTimeData.setLayout(new BoxLayout(manLifeTimeData, BoxLayout.Y_AXIS));

        pan.add(myLabel);
        pan.add(inputText);
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        simTimeLabel.setSize(800, 60);
        simStartTime = System.currentTimeMillis();

        /* Блок кнопок */

        buttonsPanel.setSize(800, 100);
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(showObjectsButton);
        buttonsPanel.add(changeThemeButton);
        buttonsPanel.add(saveDBButton);
        buttonsPanel.add(loadDBButton);
        buttonsPanel.add(checkBox);
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

        nMenu = new JMenu("Текст");
        nMenu.setFont(font);
        fileMenu.add(nMenu);

        showTextItem = new JMenuItem("Показать");
        showTextItem.setFont(font);
        nMenu.add(showTextItem);

        hideTextItem = new JMenuItem("Убрать");
        hideTextItem.setFont(font);
        nMenu.add(hideTextItem);

        newMenu = new JMenu("AI");
        newMenu.setFont(font);
        fileMenu.add(newMenu);

        onDevAI = new JMenuItem("Включить Dev AI");
        onDevAI.setFont(font);
        newMenu.add(onDevAI);

        offDevAI = new JMenuItem("Выключить Dev AI");
        offDevAI.setFont(font);
        newMenu.add(offDevAI);

        onManAI = new JMenuItem("Включить Dev AI");
        onManAI.setFont(font);
        newMenu.add(onManAI);

        offManAI = new JMenuItem("Выключить Dev AI");
        offManAI.setFont(font);
        newMenu.add(offManAI);

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
        setTheme();

        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible( true );
        frame.repaint();

        devAI = new DevAI(emplList);
        manAI = new ManAI(emplList);

        devAI.start();
        manAI.start();

        dbMethods = new dbMethods();
        start();
    }

    void start() {
        handleKeyActions();
        changeSimTime();
        clearTimer();
    }

    private void changeColors() {
        if (isLightTheme) {
            BUTTON_COLOR =  new Color(50, 205, 50);
            BACK_COLOR =  new Color(64, 181, 173);
            HEADER_COLOR =  new Color(135, 206, 235);
            STOP_COLOR =  new Color(255, 79, 91);
            INFO_COLOR = new Color(159, 226, 191);
            OBJ_COLOR = new Color(240, 230, 140);
            GOOD_COLOR = new Color(83, 128, 213);
            isLightTheme = false;
        } else {
            BUTTON_COLOR = Color.WHITE;
            BACK_COLOR = Color.BLACK;
            HEADER_COLOR = new Color(40, 40, 40);
            STOP_COLOR = Color.WHITE;
            INFO_COLOR = new Color(61, 65, 61);
            OBJ_COLOR = Color.WHITE;
            GOOD_COLOR = Color.WHITE;
            isLightTheme = true;
        }
    }

    private void setTheme() {
        changeColors();
        startButton.setBackground(BUTTON_COLOR);
        stopButton.setBackground(STOP_COLOR);
        showObjectsButton.setBackground(OBJ_COLOR);
        changeThemeButton.setBackground(GOOD_COLOR);
        saveDBButton.setBackground(GOOD_COLOR);
        loadDBButton.setBackground(GOOD_COLOR);
        checkBox.setBackground(LIGHT_COLOR);
        buttonsPanel.setBackground(HEADER_COLOR);
        devLifeTimeData.setBackground(HEADER_COLOR);
        manLifeTimeData.setBackground(HEADER_COLOR);
        simTimeLabel.setBackground(BACK_COLOR);
        objectsPanel.setBackground(BACK_COLOR);
        pan.setBackground(HEADER_COLOR);
        objectsPanel.repaint();
    }

    void deleteDeadObjects(long currTime) {

        for (int i = 0; i < emplList.size(); i++) {
            Employees obj = emplList.get(i);

            if (obj.LayObject != null) {
                if (currTime - obj.getCreateTime() >= Developer.devLifeTime*1000 && obj instanceof Developer) {
                    objectsPanel.remove(obj.LayObject);
                    emplList.remove(obj);
                    IdList.remove(obj.getId());
                    hashMap.remove(obj.getId());
                }
                if (currTime - obj.getCreateTime() >= Manager.manLifeTime*1000 && obj instanceof Manager) {
                    objectsPanel.remove(obj.LayObject);
                    emplList.remove(obj);
                    IdList.remove(obj.getId());
                    hashMap.remove(obj.getId());
                }
            }

        }
    }

    public void changeSimTime() {
        simulationTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                long tmp = System.currentTimeMillis();
                long dif = (tmp - simStartTime) / 1000;
                simTime = (int)dif;
                simTimeLabel.setText("Время симуляции: " + dif);
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

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
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
            hashMap.put(newObj.getId(), ((int)(System.currentTimeMillis() - newObj.getCreateTime()))/1000);
            objectsPanel.add(newObj.LayObject);
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
            hashMap.put(newObj.getId(), ((int)(System.currentTimeMillis() - simStartTime)/1000));
            objectsPanel.add(newObj.LayObject);
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

    void createObjectsModal() {
        final var objModal = new JDialog(frame, "Текущие объекты", false);
        final var textArea = new JTextArea(5, 20);

        objModal.setSize(500,300);
        textArea.setEditable(false);

        for (String name : hashMap.keySet()) {
            String key = name.toString();
            String value = hashMap.get(name).toString();
            textArea.append(" ID: " + key + ": " + value + "\n");
        }

        textArea.setBounds(1, 1, 500, 100);
        objModal.getContentPane().add(textArea);
        objModal.setLocationRelativeTo(null);
        objModal.setVisible(true);
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

    private void addEmp(Employees newObj) {
        IdList.add(newObj.getId());
        hashMap.put(newObj.getId(), ((int)(System.currentTimeMillis() - newObj.getCreateTime()))/1000);
        objectsPanel.add(newObj.LayObject);
    }

    private void handleKeyActions() {
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

        showObjectsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createObjectsModal();
            }
        });

        changeThemeButton.addActionListener(e -> setTheme());
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

        showTextItem.addActionListener(e -> onShow());
        hideTextItem.addActionListener(e -> onHide());
        onDevAI.addActionListener(e -> devAI.resumeAI());
        offDevAI.addActionListener(e -> devAI.stopAI());
        onManAI.addActionListener(e -> manAI.resumeAI());
        offManAI.addActionListener(e -> manAI.stopAI());
        saveDBButton.addActionListener(e -> dbMethods.saveObjects(emplList));

        loadDBButton.addActionListener(e -> {
            objectsPanel.removeAll();
            hashMap.clear();
            IdList.clear();
            emplList.clear();
            var tmp = dbMethods.loadObjects();

            for (int i = 0; i < tmp.size(); i++) {
                emplList.add(tmp.get(i));
                addEmp(tmp.get(i));
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
            modalOKButton.setBackground(BUTTON_COLOR);
            modalCancelButton.setBackground(STOP_COLOR);
            pane.add(modalOKButton);
            pane.add(modalCancelButton);
            modal.setVisible(true);

            return;
        }
        onStop();
    }

    public static void main(String[] args)
    {
        var beg = new Environment();
    }
}
