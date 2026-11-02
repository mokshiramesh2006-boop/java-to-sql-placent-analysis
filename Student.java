package com.eligisync.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Locale;

@Entity
@Table(name = "students")
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Student ID is required")
    @Column(nullable = false, unique = true)
    private String studentId;

    @Email(message = "Enter a valid email")
    private String email;

    @DecimalMin("0.0") @DecimalMax("10.0")
    private double cgpa;

    @Min(0)
    private int backlogs;

    @Column(length = 1200)
    private String skills = "";

    private boolean placed;

    public Student() {}

    public Student(String name, String studentId, String email, double cgpa,
                   int backlogs, String skills, boolean placed) {
        this.name = name; this.studentId = studentId; this.email = email;
        this.cgpa = cgpa; this.backlogs = backlogs; this.skills = skills;
        this.placed = placed;
    }

    public boolean hasSkill(String skill) {
        if (skill == null || skill.isBlank()) return true;
        String haystack = skills == null ? "" : skills.toLowerCase(Locale.ROOT);
        for (String item : skill.split(",")) {
            if (!haystack.contains(item.trim().toLowerCase(Locale.ROOT))) return false;
        }
        return true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }
    public int getBacklogs() { return backlogs; }
    public void setBacklogs(int backlogs) { this.backlogs = backlogs; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public boolean isPlaced() { return placed; }
    public void setPlaced(boolean placed) { this.placed = placed; }
}
