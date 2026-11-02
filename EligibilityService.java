package com.eligisync.service;

import com.eligisync.model.Company;
import com.eligisync.model.Student;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EligibilityService {
    public boolean isEligible(Student student, Company company) {
        return student.getCgpa() >= company.getMinCgpa()
            && student.getBacklogs() <= company.getMaxBacklogs()
            && student.hasSkill(company.getRequiredSkills());
    }

    // Transparent demo score: 60% CGPA, 25% required-skill match, 15% backlog status.
    // This is a configurable-project starting point, not a universal hiring standard.
    public int matchingScore(Student student, Company company) {
        double cgpaScore = Math.max(0, Math.min(1, student.getCgpa() / 10.0));
        List<String> skills = Arrays.stream(
                Optional.ofNullable(company.getRequiredSkills()).orElse("")
                    .split(","))
            .map(String::trim).filter(s -> !s.isBlank()).toList();
        double skillScore = skills.isEmpty() ? 1.0 :
            (double) skills.stream().filter(student::hasSkill).count() / skills.size();
        double backlogScore = student.getBacklogs() == 0 ? 1.0 :
            Math.max(0, 1.0 - (student.getBacklogs() * 0.25));
        return (int)Math.round(100 * (0.60 * cgpaScore + 0.25 * skillScore + 0.15 * backlogScore));
    }

    public List<Student> rankEligible(List<Student> students, Company company) {
        return students.stream().filter(s -> isEligible(s, company))
            .sorted(Comparator.comparingInt((Student s) -> matchingScore(s, company)).reversed()
                .thenComparing(Student::getName, String.CASE_INSENSITIVE_ORDER))
            .collect(Collectors.toList());
    }

    public List<String> missingSkills(Student student, Company company) {
        return Arrays.stream(Optional.ofNullable(company.getRequiredSkills()).orElse("").split(","))
            .map(String::trim).filter(s -> !s.isBlank()).filter(s -> !student.hasSkill(s))
            .distinct().collect(Collectors.toList());
    }
}
