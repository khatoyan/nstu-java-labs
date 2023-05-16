package DB;

import Employees.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Methods {
    public void saveObjects(List<Employees> cars) {
        {
            try {
                try {
                    Class.forName("org.sqlite.JDBC");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                Connection connection = DriverManager.getConnection("jdbc:sqlite:objects.db");
                createTableIfNeeded(connection);
                connection.createStatement().execute("DELETE FROM objects;");
                cars.forEach(car -> {
                    String sql = "INSERT INTO objects values ('";
                    sql += car.getId();
                    sql += "', ";
                    sql += car.getX();
                    sql += ", ";
                    sql += car.getY();
                    sql += ", ";
                    sql += car.getCreateTime();
                    sql += ", ";
                    sql += car.getLifetime();
                    sql += ", ";
                    if (car instanceof Manager) {
                        sql += "'manager'";
                    } else {
                        sql += "'developer'";
                    }
                    sql += ");";

                    try {
                        connection.createStatement().execute(sql);
                        System.out.println("Success!");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public List<Employees> loadObjects() {
        ArrayList<Employees> cars = new ArrayList<>();

        try {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Connection connection = DriverManager.getConnection("jdbc:sqlite:objects.db");
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM objects;");

            while (result.next()) {
                if (result.getString("type").equals("manager")) {
                    cars.add(new Manager(
                            result.getString("id").replaceAll("-", "").substring(0, 4),
                            result.getInt("x"),
                            result.getInt("y"),
                            result.getLong("birthTime"),
                            result.getLong("lifetime")
                    ));
                } else {
                    cars.add(new Developer(
                            result.getString("id").replaceAll("-", "").substring(0, 4),
                            result.getInt("x"),
                            result.getInt("y"),
                            result.getLong("birthTime"),
                            result.getLong("lifetime")
                    ));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cars;
    }

    private void createTableIfNeeded(Connection connection) throws SQLException {
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS objects\n" +
                "(\n" +
                "    id        TEXT PRIMARY KEY,\n" +
                "    x         INTEGER    NOT NULL,\n" +
                "    y         INTEGER    NOT NULL,\n" +
                "    birthTime INTEGER NOT NULL,\n" +
                "    lifetime  INTEGER NOT NULL,\n" +
                "    type       TEXT    NOT NULL\n" +
                ");");
    }
}