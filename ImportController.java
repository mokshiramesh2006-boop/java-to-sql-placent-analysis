package com.eligisync.controller;

import com.eligisync.model.Student;
import com.eligisync.repository.StudentRepository;
import com.opencsv.CSVReader;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;

@Controller
public class ImportController {
    private final StudentRepository students;
    public ImportController(StudentRepository students) { this.students = students; }

    @GetMapping("/imports")
    public String imports() { return "imports"; }

    @PostMapping("/imports/csv")
    public String importCsv(@RequestParam("file") MultipartFile file, RedirectAttributes flash) {
        if (file.isEmpty() || !file.getOriginalFilename().toLowerCase(Locale.ROOT).endsWith(".csv")) {
            flash.addFlashAttribute("error", "Please select a non-empty .csv file.");
            return "redirect:/imports";
        }
        int saved = 0, skipped = 0;
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] header = reader.readNext();
            if (header == null) throw new IllegalArgumentException("CSV is empty.");
            Map<String,Integer> columns = new HashMap<>();
            for (int i=0;i<header.length;i++) columns.put(header[i].trim().toLowerCase(Locale.ROOT), i);
            for (String required : List.of("name","studentid","cgpa"))
                if (!columns.containsKey(required)) throw new IllegalArgumentException("Missing required CSV column: " + required);
            String[] row;
            while ((row = reader.readNext()) != null) {
                try {
                    String id = value(row, columns, "studentid");
                    if (id.isBlank() || students.findByStudentId(id).isPresent()) { skipped++; continue; }
                    Student s = new Student(value(row,columns,"name"), id, value(row,columns,"email"),
                        Double.parseDouble(value(row,columns,"cgpa")), integer(value(row,columns,"backlogs")),
                        value(row,columns,"skills"), Boolean.parseBoolean(value(row,columns,"placed")));
                    students.save(s); saved++;
                } catch (Exception badRow) { skipped++; }
            }
            flash.addFlashAttribute("success", "CSV import complete. Saved: " + saved + ", skipped: " + skipped + ".");
        } catch (Exception e) { flash.addFlashAttribute("error", "CSV import failed: " + e.getMessage()); }
        return "redirect:/imports";
    }

    @PostMapping("/imports/resume")
    public String importResume(@RequestParam("file") MultipartFile file, RedirectAttributes flash) {
        if (file.isEmpty()) { flash.addFlashAttribute("error", "Choose a PDF or DOCX resume."); return "redirect:/imports"; }
        String filename = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase(Locale.ROOT);
        try {
            String text;
            if (filename.endsWith(".pdf")) {
                try (var pdf = Loader.loadPDF(file.getBytes())) { text = new PDFTextStripper().getText(pdf); }
            } else if (filename.endsWith(".docx")) {
                try (XWPFDocument doc = new XWPFDocument(file.getInputStream())) {
                    StringBuilder b = new StringBuilder(); doc.getParagraphs().forEach(x -> b.append(x.getText()).append('\n')); text = b.toString();
                }
            } else throw new IllegalArgumentException("Only PDF and DOCX files are supported.");

            String email = match(text, "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}");
            String name = firstUsefulLine(text);
            String skills = extractSkills(text);
            String studentId = email.isBlank() ? "RES-" + UUID.randomUUID().toString().substring(0,8) : "RES-" + email.toLowerCase(Locale.ROOT);
            if (students.findByStudentId(studentId).isPresent()) throw new IllegalArgumentException("This resume appears to have been imported already.");
            double cgpa = parseCgpa(text);
            Student s = new Student(name, studentId, email.isBlank()?null:email, cgpa, 0, skills, false);
            students.save(s);
            flash.addFlashAttribute("success", "Resume parsed and saved for " + name + ". Please review the student record and correct any extracted details.");
        } catch (Exception e) { flash.addFlashAttribute("error", "Resume import failed: " + e.getMessage()); }
        return "redirect:/students";
    }

    private static String value(String[] row, Map<String,Integer> c, String key) {
        Integer i=c.get(key); return i==null || i>=row.length || row[i]==null ? "" : row[i].trim();
    }
    private static int integer(String s) { return s.isBlank()?0:Integer.parseInt(s); }
    private static String match(String text, String regex) { Matcher m=Pattern.compile(regex,Pattern.CASE_INSENSITIVE).matcher(text); return m.find()?m.group():""; }
    private static String firstUsefulLine(String text) {
        return Arrays.stream(text.split("\\R")).map(String::trim).filter(x -> x.length()>2 && x.length()<65 && !x.contains("@") && !x.toLowerCase().contains("resume") && !x.toLowerCase().contains("curriculum vitae")).findFirst().orElse("Resume candidate");
    }
    private static double parseCgpa(String text) {
        Matcher m=Pattern.compile("(?i)(?:CGPA|GPA)\\s*[:=-]?\\s*(10(?:\\.0+)?|[0-9](?:\\.[0-9]{1,2})?)").matcher(text);
        if(m.find()) try { double v=Double.parseDouble(m.group(1)); if(v>=0 && v<=10)return v; } catch(Exception ignored){} return 0.0;
    }
    private static String extractSkills(String text) {
        List<String> known=List.of("Java","Python","C++","C","SQL","HTML","CSS","JavaScript","Spring Boot","Machine Learning","Excel","React","Git","AWS","Communication");
        String lower=text.toLowerCase(Locale.ROOT); return String.join(", ", known.stream().filter(k -> lower.contains(k.toLowerCase(Locale.ROOT))).toList());
    }
}
