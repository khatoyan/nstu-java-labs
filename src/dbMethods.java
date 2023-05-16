import Employees.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class dbMethods {
    public void saveObjects(List<Employees> empList) {
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
                empList.forEach(emp -> {
                    String sql = "INSERT INTO objects values ('";
                    sql += emp.getId();
                    sql += "', ";
                    sql += emp.getX();
                    sql += ", ";
                    sql += emp.getY();
                    sql += ", ";
                    sql += emp.getCreateTime();
                    sql += ", ";
                    sql += emp.getLifetime();
                    sql += ", ";
                    if (emp instanceof Manager) {
                        sql += "'manager'";
                    } else {
                        sql += "'developer'";
                    }
                    sql += ");";

                    try {
                        connection.createStatement().execute(sql);
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
        ArrayList<Employees> empList = new ArrayList<>();

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
                    empList.add(new Manager(
                            result.getString("id").replaceAll("-", "").substring(0, 4),
                            result.getInt("x"),
                            result.getInt("y"),
                            result.getLong("birthTime"),
                            result.getLong("lifetime")
                    ));
                } else {
                    empList.add(new Developer(
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

        return empList;
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