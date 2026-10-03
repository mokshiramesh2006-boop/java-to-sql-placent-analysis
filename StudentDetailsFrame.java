package placement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class StudentDetailsFrame {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Student Details");

        String[] columns = {
            "ID", "Name", "Email", "Branch",
            "CGPA", "Backlogs", "Skills",
            "Certification", "Aptitude", "Communication"
        };

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);

        JTable table = new JTable(model);
        JScrollPane pane = new JScrollPane(table);

        pane.setBounds(20, 20, 900, 300);

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/test",
                    "root",
                    "Mokshi@123");

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(
                    "SELECT * FROM student");

            while (rs.next()) {

                model.addRow(new Object[] {
                    rs.getInt("student_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("branch"),
                    rs.getDouble("cgpa"),
                    rs.getInt("backlogs"),
                    rs.getString("skills"),
                    rs.getString("certification"),
                    rs.getInt("aptitude_score"),
                    rs.getString("communication")
                });
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        frame.add(pane);

        frame.setSize(1000, 400);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
