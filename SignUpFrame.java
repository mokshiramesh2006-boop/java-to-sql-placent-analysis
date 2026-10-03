package placement;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SignUpFrame extends JFrame {

    JTextField studentIdField, nameField, emailField, branchField;
    JTextField cgpaField, backlogsField, skillsField, certificationsField;
    JTextField aptitudeField, communicationField, internshipsField;
    JPasswordField passwordField;
    JButton signupButton;

    public SignUpFrame() {

        setTitle("Student Sign Up");
        setSize(500, 600);
        setLayout(new GridLayout(12, 2, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        add(studentIdField);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Branch:"));
        branchField = new JTextField();
        add(branchField);

        add(new JLabel("CGPA:"));
        cgpaField = new JTextField();
        add(cgpaField);

        add(new JLabel("Backlogs:"));
        backlogsField = new JTextField();
        add(backlogsField);

        add(new JLabel("Skills:"));
        skillsField = new JTextField();
        add(skillsField);

        add(new JLabel("Certifications:"));
        certificationsField = new JTextField();
        add(certificationsField);

        add(new JLabel("Aptitude Score:"));
        aptitudeField = new JTextField();
        add(aptitudeField);

        add(new JLabel("Communication Score:"));
        communicationField = new JTextField();
        add(communicationField);

        add(new JLabel("Internships:"));
        internshipsField = new JTextField();
        add(internshipsField);

        signupButton = new JButton("Sign Up");
        add(new JLabel(""));
        add(signupButton);

        signupButton.addActionListener(e -> signup());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void signup() {

        try {
            int studentId = Integer.parseInt(studentIdField.getText());
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String branch = branchField.getText();

            double cgpa = Double.parseDouble(cgpaField.getText());
            int backlogs = Integer.parseInt(backlogsField.getText());

            String skills = skillsField.getText();
            String certifications = certificationsField.getText();

            double aptitudeScore =
                    Double.parseDouble(aptitudeField.getText());

            double communicationScore =
                    Double.parseDouble(communicationField.getText());

            int internships =
                    Integer.parseInt(internshipsField.getText());

            String sql = "INSERT INTO student "
                    + "(student_id, name, email, password, branch, cgpa, "
                    + "back"
                    + "logs, skills, certifications, aptitude_score, "
                    + "communication_score, internships) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, studentId);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setString(5, branch);
            ps.setDouble(6, cgpa);
            ps.setInt(7, backlogs);
            ps.setString(8, skills);
            ps.setString(9, certifications);
            ps.setDouble(10, aptitudeScore);
            ps.setDouble(11, communicationScore);
            ps.setInt(12, internships);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Signup Successful!");

            ps.close();
            con.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Signup Failed: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new SignUpFrame();
    }
}
