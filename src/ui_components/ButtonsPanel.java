package ui_components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonsPanel extends JPanel {
    public final JButton start = new JButton("start");
    private final JButton showButton = new JButton("show");
    private final JButton stopButton = new JButton("Stop/Resume");
    //private final JButton stopAllButton = new JButton("Stop all");
    //private final JButton resumeAllButton = new JButton("Resume all");

    public ButtonsPanel() {
        setLayout(new GridLayout(1, 7));
        setButtonSize(start);
        setButtonSize(showButton);
        setButtonSize(stopButton);
//        setButtonSize(stopAllButton);
//        setButtonSize(resumeAllButton);
//        add(addButton);
        add(start);
        add(stopButton);
        add(showButton);
        //add(resumeAllButton);
    }

    public void setButtonSize(JButton button) {
        button.setSize(50, 50);
    }

//    public void onAddButtonClicked(ActionListener listener) {
//        addButton.addActionListener(listener);
//    }

//    public void onRemoveButtonClicked(ActionListener listener) {
//        removeButton.addActionListener(listener);
//    }

    public void onStopButtonClicked(ActionListener listener) {
        stopButton.addActionListener(listener);
    }

    public void onShowButtonClicked(ActionListener listener) {
        showButton.addActionListener(listener);
    }

    public void onStartButtonClicked(ActionListener listener) {
        start.addActionListener(listener);
    }
}