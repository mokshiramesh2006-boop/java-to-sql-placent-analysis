package com.eligisync.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "companies")
public class Company {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Company name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Role is required")
    private String role;

    @DecimalMin("0.0") @DecimalMax("10.0")
    private double minCgpa = 7.0;

    @Min(0)
    private int maxBacklogs = 0;

    @Column(length = 1200)
    private String requiredSkills = "";

    private double packageLpa;

    public Company() {}
    public Company(String name, String role, double minCgpa, int maxBacklogs,
                   String requiredSkills, double packageLpa) {
        this.name = name; this.role = role; this.minCgpa = minCgpa;
        this.maxBacklogs = maxBacklogs; this.requiredSkills = requiredSkills;
        this.packageLpa = packageLpa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(double minCgpa) { this.minCgpa = minCgpa; }
    public int getMaxBacklogs() { return maxBacklogs; }
    public void setMaxBacklogs(int maxBacklogs) { this.maxBacklogs = maxBacklogs; }
    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }
    public double getPackageLpa() { return packageLpa; }
    public void setPackageLpa(double packageLpa) { this.packageLpa = packageLpa; }
}
