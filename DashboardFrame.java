
package placement;

import javax.swing.*;
import java.awt.event.*;

public class DashboardFrame {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Placement Analytics Dashboard");

        JLabel title = new JLabel("Welcome to Placement Analytics");
        title.setBounds(80, 30, 250, 30);

        JButton studentBtn = new JButton("Student Details");
        studentBtn.setBounds(100, 80, 180, 30);

        JButton companyBtn = new JButton("Company Details");
        companyBtn.setBounds(100, 130, 180, 30);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(100, 180, 180, 30);

        frame.add(title);
        frame.add(studentBtn);
        frame.add(companyBtn);
        frame.add(logoutBtn);

        studentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "Student Details Page");
            }
        });

        companyBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "Company Details Page");
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                LoginFrame.main(null);
            }
        });

        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}