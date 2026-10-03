package placement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class SignupStudent {

    public static void main(String[] args) {

        String url = "jdbc:mysql://127.0.0.1:3306/placement_analytics";
        String username = "root";
        String password = "Mokshi@123"; 

        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Password: ");
            String pass = sc.nextLine();

            System.out.print("Enter Branch: ");
            String branch = sc.nextLine();

            System.out.print("Enter CGPA: ");
            double cgpa = sc.nextDouble();

            System.out.print("Enter Backlogs: ");
            int backlogs = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Skills: ");
            String skills = sc.nextLine();

            System.out.print("Enter Certifications: ");
            String certifications = sc.nextLine();

            Connection con = DriverManager.getConnection(url, username, password);

            String query = "INSERT INTO student(name,email,password,branch,cgpa,backlogs,skills,certifications) VALUES(?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, pass);
            ps.setString(4, branch);
            ps.setDouble(5, cgpa);
            ps.setInt(6, backlogs);
            ps.setString(7, skills);
            ps.setString(8, certifications);

            ps.executeUpdate();

            System.out.println("Student Registered Successfully!");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();
    }
}
