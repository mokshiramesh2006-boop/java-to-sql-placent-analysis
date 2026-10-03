package placement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ReadStudent {

    public static void main(String[] args) {

        String url = "jdbc:mysql://127.0.0.1:3306/placement_analytics";
        String username = "root";
        String password = "Mokshi@123";

        try {
            Connection con = DriverManager.getConnection(url, username, password);

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM student");

            while(rs.next()) {
                System.out.println("ID: " + rs.getInt("student_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("CGPA: " + rs.getDouble("cgpa"));
                System.out.println("----------------");
            }

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
