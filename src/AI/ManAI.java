package AI;


import Employees.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.ListIterator;

public class ManAI extends BaseAI {

    public ManAI(ArrayList<Employees> objects) {
        super(objects, "ManAI");
    }

    @Override
    Point move(Employees obj) {
        float newX = obj.getX(), newY = obj.getY();
        double angle = getAngle(obj);

        newX -= velocity * Math.cos(angle);
        newY -= velocity * Math.sin(angle);

        return new Point(Math.round(newX), Math.round(newY));
    }

    @Override
    synchronized void procces() {
        if (objects.size() != 0) {
            try {
                for (int i = 0; i < objects.size(); i++) {
                    Employees emp = objects.get(i);
                    if (emp instanceof Manager && checkPos(emp)) {
                        Point p = move(emp);
                        emp.setPosition(p.x, p.y);
                    }
                }
            } catch (Exception e) {
                System.out.println("Man Process Error\n");
                return;
            }
        }
    }

    @Override
    boolean checkPos(Employees obj) {
        return true;
    }
}