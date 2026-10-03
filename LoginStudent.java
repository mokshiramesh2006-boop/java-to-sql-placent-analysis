package placement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class LoginStudent {

    public static void main(String[] args) {

        String url = "jdbc:mysql://127.0.0.1:3306/placement_analytics";
        String username = "root";
        String password = "Mokshi@123";   

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        try {
            Connection con = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM student WHERE email=? AND password=?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, email);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login Successful!");
                System.out.println("Welcome " + rs.getString("name"));
                System.out.println("CGPA: " + rs.getDouble("cgpa"));
                System.out.println("Branch: " + rs.getString("branch"));
            } else {
                System.out.println("Invalid Email or Password!");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();
    }
}
