
package placement;

import javax.swing.*;
import java.sql.*;
import java.awt.event.*;

public class LoginFrame {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Placement Analytics Login");

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 30);

        final JTextField userText = new JTextField();
        userText.setBounds(150, 50, 150, 30);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 30);

        final JPasswordField passText = new JPasswordField();
        passText.setBounds(150, 100, 150, 30);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(120, 160, 100, 30);

        frame.add(userLabel);
        frame.add(userText);
        frame.add(passLabel);
        frame.add(passText);
        frame.add(loginBtn);

        loginBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                String email = userText.getText();
                String pass = new String(passText.getPassword());

                try {

                    String url = "jdbc:mysql://127.0.0.1:3306/placement_analytics";
                    String username = "root";
                    String password = "Mokshi@123";

                    Connection con =
                            DriverManager.getConnection(url, username, password);

                    String query =
                            "SELECT * FROM student WHERE email=? AND password=?";

                    PreparedStatement ps =
                            con.prepareStatement(query);

                    ps.setString(1, email);
                    ps.setString(2, pass);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {

                        JOptionPane.showMessageDialog(
                                frame,
                                "Login Successful!\nWelcome " +
                                rs.getString("name")
                        );

                    } else {

                        JOptionPane.showMessageDialog(
                                frame,
                                "Invalid Email or Password!"
                        );
                    }

                    con.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
//test