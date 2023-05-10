package AI;

import Employees.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class BaseAI extends Thread {
    private boolean paused = false;

    int velocity = 2;
    boolean running = true;
    private final Environment context;

    ArrayList<Employees> objects;

    BaseAI(ArrayList<Employees> objects, String threadName, Environment context) {
        this.objects = objects;
        this.setName(threadName);
        this.context = context;
    }

    @Override
    public void run() {
        while (running) {
            synchronized (this) {
                context.repaintObjPanel();
                this.procces();
            }
            context.repaintObjPanel();
            try {
                if (paused) synchronized (this) {
                    this.wait();
                    context.repaintObjPanel();
                }
                else sleep(45);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopAI() {
        paused = true;
    }

    public void resumeAI() {
        paused = false;
        synchronized (this) {
            notify();
        }
    }

    double getAngle(Employees obj) {
        Point destination = new Point(0, 0);
        double a = obj.getX() - destination.getX();
        double b = obj.getY() - destination.getY();
        double distance = Math.sqrt(Math.pow(a, 2)
                + Math.pow(b, 2));
        return Math.acos((a * a + distance * distance - b * b) / (2 * a * distance));
    }

    abstract Point move(Employees obj);

    abstract void procces();

    abstract boolean checkPos(Employees obj);
}