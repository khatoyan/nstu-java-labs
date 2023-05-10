package Employees;

import AI.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.util.HashMap;
import java.util.TreeSet;

public class Environment
{
    /* Константы */

    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 800;

    /* Цвета */

    private static final Color BUTTON_COLOR =  new Color(50, 205, 50);
    private static final Color BACK_COLOR =  new Color(64, 181, 173);
    private static final Color HEADER_COLOR =  new Color(135, 206, 235);
    private static final Color STOP_COLOR =  new Color(255, 79, 91);
    private static final Color INFO_COLOR = new Color(159, 226, 191);
    private static final Color OBJ_COLOR = new Color(240, 230, 140);

    /* Изменяемые переменные */

    private final long simStartTime;
    private int generatePeriod = 5;
    private final int chanceOfBirth = 8;
    private int simTime;
    private boolean isAllowed = true;
    private boolean showInfo = false;

    /* Таймеры */

    private Timer devTimer;
    private Timer manTimer;
    private Timer simulationTimer;

    /* Коллекции данных */

    private final ArrayList<Employees> emplList = new ArrayList<>();
    private final TreeSet IdList = new TreeSet();
    private final HashMap<String, Integer> hashMap = new HashMap<>();

    /* Блок фрейма и панелей */

    private static JFrame frame;
    private final JLabel simTimeLabel;
    private final JLabel simulationInfo;
    private final JLabel manLabel;
    private final JLabel devLabel;
    private final JPanel dataPanel;
    private final JPanel mainPanel;
    private final JPanel objectsPanel;

    /* Блок кнопок и чекбоксов */

    private final JButton startButton;
    private final JButton stopButton;
    private final JButton showObjectsButton;
    private final JRadioButton showInfoButton;
    private final JRadioButton NotShowInfoButton;
    private final JCheckBox checkBox;
    private final JTextField inputText;
    private final JTextField devLifeTime;
    private final JTextField manLifeTime;

    private final String[] items = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private final String[] manItems = items;

    private final JComboBox comboBox = new JComboBox(items);
    private final JComboBox comboBoxMan = new JComboBox(manItems);

    private JCheckBox priorCheckBox = new JCheckBox("Приоритет");
    private JCheckBox manAICheckBox = new JCheckBox("Man AI");
    private JCheckBox devAICheckBox = new JCheckBox("Dev AI");

    /* Блок с меню */

    private final JMenuItem showTextItem;
    private final JMenuItem hideTextItem;
    private final JMenuItem startItem;
    private final JMenuItem stopItem;
    private final JMenuItem exitItem;

    private final ManAI manAI;
    private final DevAI devAI;

    Environment()
    {
        frame = new JFrame("Lab 1");
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
        frame.setFocusable(true);

        JPanel devLifeTimeData = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel();
        JPanel manLifeTimeData = new JPanel(new BorderLayout());
        JPanel manPList = new JPanel(new BorderLayout());
        JPanel devPList = new JPanel(new BorderLayout());

        JPanel pan = new JPanel(new BorderLayout());
        JLabel myLabel = new JLabel("Введите время рождения в секундах: ");
        JLabel devLifeTimeLabel = new JLabel("Время жизни разрабов");
        JLabel manLifeTimeLabel = new JLabel("Время жизни менеджеров");
        manLabel = new JLabel("Man Priority");
        devLabel = new JLabel("Dev Priority");

        manPList.add(manLabel);
        manPList.add(comboBoxMan);
        devPList.add(devLabel);
        devPList.add(comboBox);

        manPList.setLayout(new BoxLayout(manPList, BoxLayout.Y_AXIS));
        devPList.setLayout(new BoxLayout(devPList, BoxLayout.Y_AXIS));

        Font font = new Font("Verdana", Font.PLAIN, 11);

        dataPanel = new JPanel();
        mainPanel = new JPanel();
        objectsPanel = new JPanel(null);
        objectsPanel.setPreferredSize(new Dimension(SCREEN_WIDTH, 500));

        simTimeLabel = new JLabel();
        simulationInfo = new JLabel();

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        showObjectsButton = new JButton("Текущие объекты");

        NotShowInfoButton = new JRadioButton("Убрать текст", false);
        showInfoButton = new JRadioButton("Показать текст", true);
        ButtonGroup switchButtons = new ButtonGroup();
        checkBox = new JCheckBox("Показывать информацию");
        inputText = new JTextField( 25);
        devLifeTime = new JTextField( 25);
        manLifeTime = new JTextField( 25);

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
        buttonsPanel.add(showObjectsButton);
        buttonsPanel.add(checkBox);
        buttonsPanel.add(showInfoButton);
        buttonsPanel.add(NotShowInfoButton);
        buttonsPanel.add(pan);

        buttonsPanel.add(devLifeTimeData);
        buttonsPanel.add(manLifeTimeData);

        buttonsPanel.add(manPList);
        buttonsPanel.add(devPList);
        buttonsPanel.add(manAICheckBox);
        buttonsPanel.add(devAICheckBox);

        /* Главная панель */

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(buttonsPanel);
        mainPanel.add(objectsPanel);
        mainPanel.add(simTimeLabel);

        /* Меню */

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Настройки");
        fileMenu.setFont(font);

        JMenu newMenu = new JMenu("Текст");
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
        showObjectsButton.setBackground(OBJ_COLOR);
        checkBox.setBackground(HEADER_COLOR);
        buttonsPanel.setBackground(HEADER_COLOR);
        devLifeTimeData.setBackground(HEADER_COLOR);
        manLifeTimeData.setBackground(HEADER_COLOR);
        NotShowInfoButton.setBackground(HEADER_COLOR);
        showInfoButton.setBackground(HEADER_COLOR);
        simTimeLabel.setBackground(BACK_COLOR);
        objectsPanel.setBackground(BACK_COLOR);

        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(mainPanel);

        manAI = new ManAI(emplList, this);
        devAI = new DevAI(emplList, this);

        frame.setLocationRelativeTo(null);
        frame.setVisible( true );
        frame.repaint();
        start();
    }

    public void repaintObjPanel() {
        objectsPanel.repaint();
        objectsPanel.revalidate();
        frame.repaint();
    }

    void start() {
        handleKeyActions();
        changeSimTime();
        clearTimer();
        generate();

        priorCheckBox.addActionListener(e -> {
            if (!priorCheckBox.isSelected()) {
                manAI.setPriority(Thread.MAX_PRIORITY);
                devAI.setPriority(Thread.MIN_PRIORITY);
            } else {
                manAI.setPriority(Thread.MIN_PRIORITY);
                devAI.setPriority(Thread.MAX_PRIORITY);
            }
        });

        devAI.start();
        manAI.start();
        attachListeners();
    }

    private void attachListeners() {
        devAICheckBox.setSelected(true);
        manAICheckBox.setSelected(true);
        devAICheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlDevThread(devAICheckBox.isSelected());
            }
        });

        manAICheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlManThread(manAICheckBox.isSelected());
            }
        });
    }

    boolean fistCheck = true;
    private void controlManThread(boolean checked) {
        if (!checked) {
            manAI.stopAI();
        } else {
            manAI.resumeAI();
        }
    }

    private void controlDevThread(boolean checked) {
        if (!checked) {
            devAI.stopAI();
        } else {
            devAI.resumeAI();
        }
    }

    void deleteDeadObjects(long currTime) {
        for (int i = 0; i < emplList.size(); i++) {
            Employees obj = emplList.get(i);

            if (obj.LayObject != null) {
                if (currTime - obj.getCreateTime() >= Developer.devLifeTime*1000 && obj instanceof Developer) {
                    objectsPanel.remove(obj.LayObject);
                    hashMap.remove(obj.getId());
                    emplList.remove(obj);
                    IdList.remove(obj.getId());
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

    public void toRunTimer() {

        Timer runTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteDeadObjects(System.currentTimeMillis());
                frame.repaint();
            }
        });

        runTimer.start();
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
                    getRandomNumber(0,  objectsPanel.getHeight() - Employees.IMAGE_HEIGHT));

            emplList.add(newObj);
            IdList.add(newObj.getId());
            hashMap.put(newObj.getId(), ((int)(System.currentTimeMillis() - newObj.getCreateTime()))/1000);
            objectsPanel.add(newObj.LayObject);
            frame.repaint();
        }

        if (rand2 < chanceOfBirth && objType == 2) {

            if (!isAllowed) {
                return -1;
            }

            var newObj = new Manager(
                    getRandomNumber(0, objectsPanel.getWidth() - Employees.IMAGE_WIDTH),
                    getRandomNumber(0,  objectsPanel.getHeight() - Employees.IMAGE_HEIGHT));

            emplList.add(newObj);
            IdList.add(newObj.getId());
            hashMap.put(newObj.getId(), ((int)(System.currentTimeMillis() - simStartTime)/1000));
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
                frame.repaint();
                create(simStartTime, 1);
            }
        };

        var manAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isAllowed) return;
                frame.repaint();
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
        textArea.removeAll();

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

        showInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onShow();
            }
        });

        NotShowInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHide();}
        });
    }

    void handleMenuButtons() {

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox)e.getSource();
                String item = (String)box.getSelectedItem();
                devAI.setPriority(Integer.parseInt(item));
            }
        };

        ActionListener manActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox)e.getSource();
                String item = (String)box.getSelectedItem();
                manAI.setPriority(Integer.parseInt(item));
            }
        };

        comboBox.addActionListener(actionListener);
        comboBoxMan.addActionListener(actionListener);
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
                } catch (NumberFormatException ex) {
                    generatePeriod = 1;
                    devTimer.setDelay(generatePeriod*1000);
                    manTimer.setDelay(generatePeriod*1000);
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
