package com.eligisync.config;

import com.eligisync.model.Company;
import com.eligisync.model.Student;
import com.eligisync.repository.CompanyRepository;
import com.eligisync.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/** Loads clearly fictional demonstration records once, into an empty EligiSync database. */
@Configuration
public class DemoDataLoader {
    @Bean
    CommandLineRunner loadDemoData(StudentRepository students, CompanyRepository companies) {
        return args -> {
            if (students.count() == 0) {
                String[] first = {"Aarav","Aditi","Akash","Ananya","Arjun","Diya","Ishaan","Kavya","Meera","Nikhil","Priya","Rahul","Riya","Sanjay","Sneha","Varun","Vikram","Zoya","Neha","Karan"};
                String[] last = {"Sharma","Rao","Patel","Kumar","Reddy"};
                String[] skillSets = {
                    "Java, SQL, Spring Boot, Git","Python, SQL, Excel, Pandas","JavaScript, HTML, CSS, React","Python, Machine Learning, SQL","C++, Java, Data Structures",
                    "Java, JDBC, MySQL, OOP","Python, Excel, Power BI, SQL","Java, Spring Boot, REST API","Python, NumPy, Pandas, Machine Learning","HTML, CSS, JavaScript, Git",
                    "C, C++, Data Structures, Algorithms","Java, SQL, HTML, CSS","Python, Flask, SQL, Git","JavaScript, Node.js, Express, MongoDB","Python, Data Analysis, Excel",
                    "Java, OOP, JDBC, MySQL","Python, Machine Learning, Scikit-learn","SQL, Power BI, Excel, Tableau","Java, Spring Boot, Docker","C++, Python, Problem Solving"
                };
                for (int i = 0; i < 100; i++) {
                    String name = first[i % first.length] + " " + last[(i / first.length) % last.length];
                    String id = String.format("DEMO%03d", i + 1);
                    String email = "student" + (i + 1) + "@example.test";
                    double cgpa = Math.round((6.0 + ((i * 17) % 41) / 10.0) * 100.0) / 100.0;
                    int backlogs = (i % 13 == 0) ? 1 : 0;
                    boolean placed = i % 5 == 0;
                    students.save(new Student(name, id, email, cgpa, backlogs, skillSets[i % skillSets.length], placed));
                }
            }
            if (companies.count() == 0) {
                List<Company> demoCompanies = List.of(
                    new Company("BluePeak Technologies (Demo)", "Java Developer", 7.0, 0, "Java, SQL, Spring Boot", 6.0),
                    new Company("Nexora Systems (Demo)", "Python Developer", 6.5, 1, "Python, SQL, Git", 5.5),
                    new Company("AsterByte Labs (Demo)", "Data Analyst", 7.0, 0, "SQL, Excel, Power BI", 5.0),
                    new Company("CloudHarbor Digital (Demo)", "Frontend Developer", 6.0, 1, "HTML, CSS, JavaScript", 4.8),
                    new Company("VertexNova Software (Demo)", "Full Stack Intern", 7.5, 0, "JavaScript, SQL, REST API", 7.0),
                    new Company("Cedarwave Analytics (Demo)", "ML Intern", 7.5, 0, "Python, Machine Learning, SQL", 6.5),
                    new Company("OrbitStack Solutions (Demo)", "Backend Developer", 7.0, 0, "Java, JDBC, MySQL", 6.0),
                    new Company("MapleGrid Consulting (Demo)", "BI Analyst", 6.5, 1, "Excel, SQL, Tableau", 5.2),
                    new Company("Silverline Apps (Demo)", "Web Developer", 6.0, 1, "HTML, CSS, React", 4.5),
                    new Company("QuantumLeaf Tech (Demo)", "Software Engineer", 8.0, 0, "Java, Data Structures, SQL", 8.0),
                    new Company("BrightForge AI (Demo)", "AI Intern", 7.0, 0, "Python, Machine Learning", 6.0),
                    new Company("Pinecone DataWorks (Demo)", "Data Engineering Intern", 7.0, 0, "Python, SQL, Pandas", 5.8),
                    new Company("Redwood Cyber Labs (Demo)", "Security Intern", 6.5, 1, "Python, Linux, Networking", 5.0),
                    new Company("NimbusTrail Systems (Demo)", "Cloud Support Associate", 6.0, 1, "Linux, AWS, Git", 4.7),
                    new Company("Copperfield Networks (Demo)", "C++ Developer", 7.0, 0, "C++, Data Structures", 5.7)
                );
                companies.saveAll(demoCompanies);
            }
        };
    }
}
