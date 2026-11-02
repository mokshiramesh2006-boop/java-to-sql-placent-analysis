package com.eligisync.controller;

import com.eligisync.model.Company;
import com.eligisync.model.Student;
import com.eligisync.repository.CompanyRepository;
import com.eligisync.repository.StudentRepository;
import com.eligisync.service.EligibilityService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class WebController {
    private final StudentRepository students;
    private final CompanyRepository companies;
    private final EligibilityService eligibility;

    public WebController(StudentRepository students, CompanyRepository companies,
                         EligibilityService eligibility) {
        this.students = students;
        this.companies = companies;
        this.eligibility = eligibility;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        List<Student> allStudents = students.findAll();
        List<Company> allCompanies = companies.findAll();
        long placed = allStudents.stream().filter(Student::isPlaced).count();
        model.addAttribute("studentCount", allStudents.size());
        model.addAttribute("companyCount", allCompanies.size());
        model.addAttribute("placedCount", placed);
        model.addAttribute("unplacedCount", allStudents.size() - placed);
        model.addAttribute("placementRate", allStudents.isEmpty() ? 0 :
                Math.round(placed * 1000.0 / allStudents.size()) / 10.0);
        model.addAttribute("recentStudents", allStudents.stream()
                .sorted(Comparator.comparing(Student::getId).reversed()).limit(5).toList());
        return "dashboard";
    }

    @GetMapping("/students")
    public String students(Model model, @RequestParam(required = false) String q) {
        List<Student> all = students.findAll();
        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase(Locale.ROOT);
            all = all.stream().filter(s ->
                s.getName().toLowerCase(Locale.ROOT).contains(query)
                || s.getStudentId().toLowerCase(Locale.ROOT).contains(query)
                || Optional.ofNullable(s.getSkills()).orElse("").toLowerCase(Locale.ROOT).contains(query)
            ).toList();
        }
        model.addAttribute("students", all);
        model.addAttribute("query", q == null ? "" : q);
        return "students";
    }

    @GetMapping("/students/new")
    public String newStudent(Model model) {
        model.addAttribute("student", new Student());
        return "student-form";
    }

    @PostMapping("/students")
    public String saveStudent(@Valid @ModelAttribute Student student,
                              BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) return "student-form";
        if (students.findByStudentId(student.getStudentId()).isPresent()
                && (student.getId() == null || !students.findByStudentId(student.getStudentId())
                    .get().getId().equals(student.getId()))) {
            result.rejectValue("studentId", "duplicate", "That student ID already exists.");
            return "student-form";
        }
        students.save(student);
        flash.addFlashAttribute("success", "Student saved successfully.");
        return "redirect:/students";
    }

    @GetMapping("/students/{id}/edit")
    public String editStudent(@PathVariable Long id, Model model) {
        model.addAttribute("student", students.findById(id).orElseThrow());
        return "student-form";
    }

    @PostMapping("/students/{id}/delete")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes flash) {
        students.deleteById(id);
        flash.addFlashAttribute("success", "Student deleted.");
        return "redirect:/students";
    }

    @GetMapping("/companies")
    public String companies(Model model) {
        model.addAttribute("companies", companies.findAll());
        return "companies";
    }

    @GetMapping("/companies/new")
    public String newCompany(Model model) {
        model.addAttribute("company", new Company());
        return "company-form";
    }

    @PostMapping("/companies")
    public String saveCompany(@Valid @ModelAttribute Company company,
                              BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) return "company-form";
        companies.save(company);
        flash.addFlashAttribute("success", "Company requirements saved.");
        return "redirect:/companies";
    }

    @PostMapping("/companies/{id}/delete")
    public String deleteCompany(@PathVariable Long id, RedirectAttributes flash) {
        companies.deleteById(id);
        flash.addFlashAttribute("success", "Company deleted.");
        return "redirect:/companies";
    }

    @GetMapping("/matching")
    public String matching(@RequestParam(required = false) Long companyId, Model model) {
        List<Company> allCompanies = companies.findAll();
        model.addAttribute("companies", allCompanies);
        Company selected = allCompanies.stream()
            .filter(c -> companyId != null && c.getId().equals(companyId))
            .findFirst().orElse(allCompanies.isEmpty() ? null : allCompanies.get(0));
        model.addAttribute("selectedCompany", selected);
        if (selected != null) {
            List<Student> ranked = eligibility.rankEligible(students.findAll(), selected);
            model.addAttribute("rankedStudents", ranked);
            Map<Long, Integer> scores = new HashMap<>();
            ranked.forEach(s -> scores.put(s.getId(), eligibility.matchingScore(s, selected)));
            model.addAttribute("scores", scores);
            Map<Long, List<String>> gaps = new HashMap<>();
            students.findAll().forEach(s -> gaps.put(s.getId(), eligibility.missingSkills(s, selected)));
            model.addAttribute("skillGaps", gaps);
            model.addAttribute("allStudents", students.findAll());
            model.addAttribute("eligibleCount", ranked.size());
        }
        return "matching";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        List<Student> allStudents = students.findAll();
        List<Company> allCompanies = companies.findAll();
        model.addAttribute("students", allStudents);
        model.addAttribute("companies", allCompanies);
        model.addAttribute("placed", allStudents.stream().filter(Student::isPlaced).count());
        model.addAttribute("unplaced", allStudents.stream().filter(s -> !s.isPlaced()).count());
        model.addAttribute("averageCgpa", allStudents.isEmpty() ? 0 :
            Math.round(allStudents.stream().mapToDouble(Student::getCgpa).average().orElse(0) * 100.0) / 100.0);
        return "reports";
    }

    @GetMapping("/health")
    @ResponseBody
    public Map<String, String> health() {
        return Map.of("status", "UP", "application", "EligiSync Web");
    }
}
