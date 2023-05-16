package AI;

import Employees.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ListIterator;

public class DevAI extends BaseAI {

    final int[] operation = {1, 1, 2, 3, 4};

    public DevAI(ArrayList<Employees> objects) {
        super(objects, "DevAI");
        changeOperation();
    }

    @Override
    Point move(Employees obj) {
        float newX = obj.getX(), newY = obj.getY();

        switch (operation[0])
        {
            case 1:
                newX -= 3; // Left move
                break;
            case 2:
                newX += 3; // Right
                break;
            case 3:
                newY -= 3; // Up
                break;
            default:
                newY += 3; // Default down
                break;
        }
        return new Point(Math.round(newX), Math.round(newY));
    }

    void changeOperation() {
        var timer = new Timer(2000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                operation[0] = getRandomDiceNumber();
            }
        });

        timer.start();
    }

    @Override
    synchronized void procces() {
        if (objects.size() != 0) {
            for (int  i = 0; i < objects.size(); i++) {
                Employees emp = objects.get(i);
                if (emp instanceof Developer && checkPos(emp)) {
                    Point p = move(emp);
                    emp.setPosition(p.x, p.y);
                }
            }
        }
    }

    public static int getRandomDiceNumber() {
        return (int) (Math.random() * 4) + 1;
    }

    @Override
    boolean checkPos(Employees obj) {
        if (obj.getX() - 3 < 0 || obj.getX() + 3 > 890) {
            return false;
        }

        if (obj.getY() - 3 < 0 || obj.getY() + 3 > 525) {
            return false;
        }

        return true;
    }
}