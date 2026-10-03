
package placement;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {

        try {

            String url = "jdbc:mysql://127.0.0.1:3306/placement_analytics";
            String username = "root";
            String password = "Mokshi@123";

            Connection con = DriverManager.getConnection(
                    url, username, password);

            System.out.println("Database Connected Successfully");

            return con;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        getConnection();
    }
}