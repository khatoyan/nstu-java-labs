package AI;


import Employees.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.ListIterator;

public class ManAI extends BaseAI {

    public ManAI(ArrayList<Employees> objects, Environment context) {
        super(objects, "ManAI", context);
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
            ListIterator<Employees> iterator = objects.listIterator();
            try {
                for (Employees emp = iterator.next(); iterator.hasNext(); emp = iterator.next()) {
                    if (emp instanceof Manager && checkPos(emp)) {
                        Point p = move(emp);
                        emp.setPosition(p.x, p.y);
                    }
                }
            } catch (Exception e) {
                System.out.println("Man Process Error\n");
            }
        }
    }

    @Override
    boolean checkPos(Employees obj) {
        return true;
    }
}