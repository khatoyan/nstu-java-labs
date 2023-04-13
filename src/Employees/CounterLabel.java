package Employees;

import javax.swing.*;

public class CounterLabel extends JLabel {
    int value;
    String text;


    public CounterLabel(String text, int value) {
        this.value = value;
        this.text = text;
        super.setText(text + ": " + value);
    }

    public void setText(String text) {
        this.text = text;
        super.setText(text + ": " + value);
    }

    public void setValue(int value) {
        this.value = value;
        super.setText(text + ": " + value);
    }
}