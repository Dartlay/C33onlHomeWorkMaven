package com.students.controller;

import com.students.exception.DuplicateEmailException;
import com.students.model.Group;
import com.students.model.Student;
import com.students.service.GroupService;
import com.students.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final GroupService groupService;

    @GetMapping
    public String getStudentsByGroup(
            @RequestParam(required = false, defaultValue = "1") Long groupId,
            @RequestParam(required = false, defaultValue = "name") String sort,
            Model model) {

        List<Student> students = studentService.getStudentsByGroup(groupId);
        students.sort(createComparator(sort));

        model.addAttribute("students", students);
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("selectedGroupId", groupId);
        model.addAttribute("sort", sort);

        return "students";
    }

    @PostMapping("/add")
    public String addStudent(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String email,
            @RequestParam(required = false, defaultValue = "false") boolean hasPaid,
            @RequestParam Long groupId,
            Model model) {
        try {
            if (!name.matches("^[a-zA-Z\\s'-]+$")) {
                model.addAttribute("nameError", "Invalid name format");
                return prepareErrorModel(name, surname, email, hasPaid, groupId, model);
            }

            if (!surname.matches("^[a-zA-Z\\s'-]+$")) {
                model.addAttribute("surnameError", "Invalid surname format");
                return prepareErrorModel(name, surname, email, hasPaid, groupId, model);
            }

            studentService.addStudent(name, surname, email, hasPaid, groupId);
            return "redirect:/students?groupId=" + groupId;

        } catch (DuplicateEmailException e) {
            model.addAttribute("emailError", e.getMessage());
            return prepareErrorModel(name, surname, email, hasPaid, groupId, model);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return prepareErrorModel(name, surname, email, hasPaid, groupId, model);
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "1") Long groupId) {

        studentService.removeStudent(id);
        return "redirect:/students?groupId=" + groupId;
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {

        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file");
            return "upload";
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            int lineNumber = 0;
            int successCount = 0;
            StringBuilder errors = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] data = line.split(",");

                if (data.length != 5) {
                    errors.append(String.format("Line %d: Invalid format." +
                            " Expected: name,surname,email,hasPaid,group%n", lineNumber));
                    continue;
                }

                String name = data[0].trim();
                String surname = data[1].trim();
                String email = data[2].trim();
                boolean hasPaid = Boolean.parseBoolean(data[3].trim());
                String groupName = data[4].trim();

                if (!name.matches("^[a-zA-Z\\s'-]+$")) {
                    errors.append(String.format("Line %d: Invalid name format: %s%n", lineNumber, name));
                    continue;
                }

                if (!surname.matches("^[a-zA-Z\\s'-]+$")) {
                    errors.append(String.format("Line %d: Invalid surname format: %s%n", lineNumber, surname));
                    continue;
                }

                if (!email.matches("^[^@]+@[^@]+\\.[^@]+$")) {
                    errors.append(String.format("Line %d: Invalid email format: %s%n", lineNumber, email));
                    continue;
                }

                Group group = groupService.getAllGroups().stream()
                        .filter(g -> g.getName().equalsIgnoreCase(groupName))
                        .findFirst()
                        .orElse(null);

                if (group == null) {
                    errors.append(String.format("Line %d: Group not found: %s%n", lineNumber, groupName));
                    continue;
                }

                try {
                    studentService.addStudent(name, surname, email, hasPaid, group.getId());
                    successCount++;
                } catch (Exception e) {
                    errors.append(String.format("Line %d: Error saving student:" +
                            " %s%n", lineNumber, e.getMessage()));
                }
            }

            String message = String.format("File processed. Added %d students", successCount);
            if (errors.length() > 0) {
                message += "<br><br>Errors:<br>"
                        + errors.toString().replace("\n", "<br>");
            }
            model.addAttribute("message", message);
            return "upload";
        }
    }

    @GetMapping("/download")
    public void downloadStudents(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=students.txt");

        try (PrintWriter writer = response.getWriter()) {
            for (Student student : studentService.findAll()) {
                writer.printf("%s,%s,%s%n",
                        student.getName(),
                        student.getEmail(),
                        student.getGroup().getName());
            }
        }
    }

    private Comparator<Student> createComparator(String sort) {
        return switch (sort) {
            case "email" -> Comparator.comparing(Student::getEmail);
            case "name" -> Comparator.comparing(Student::getName);
            default -> Comparator.comparing(Student::getId);
        };
    }

    private String prepareErrorModel(String name, String surname,
                                     String email, boolean hasPaid,
                                     Long groupId, Model model) {
        model.addAttribute("nameValue", name);
        model.addAttribute("surnameValue", surname);
        model.addAttribute("emailValue", email);
        model.addAttribute("hasPaidValue", hasPaid);
        model.addAttribute("students", studentService.getStudentsByGroup(groupId));
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("selectedGroupId", groupId);
        return "students";
    }
}