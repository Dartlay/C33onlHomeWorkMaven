package com.students.controller;

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
            @RequestParam String email,
            @RequestParam Long groupId,
            Model model) {

        if (!name.matches("^[a-zA-Z\\s'-]+$")) {
            model.addAttribute("nameError", "Name must contain only Latin letters");
            return prepareErrorModel(groupId, model);
        }

        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            model.addAttribute("error", "Group not found");
            return prepareErrorModel(groupId, model);
        }

        studentService.addStudent(name, email, group);
        return "redirect:/students?groupId=" + groupId;
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

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] data = line.split(",");

                if (data.length != 3) {
                    model.addAttribute("message",
                            String.format("Invalid format on line %d. Expected: name,email,group", lineNumber));
                    return "upload";
                }

                String name = data[0].trim();
                String email = data[1].trim();
                String groupName = data[2].trim();

                if (!name.matches("^[a-zA-Z\\s'-]+$")) {
                    model.addAttribute("message",
                            String.format("Invalid name on line %d: %s", lineNumber, name));
                    return "upload";
                }

                Group group = groupService.getAllGroups().stream()
                        .filter(g -> g.getName().equalsIgnoreCase(groupName))
                        .findFirst()
                        .orElse(null);

                if (group == null) {
                    model.addAttribute("message",
                            String.format("Group not found on line %d: %s", lineNumber, groupName));
                    return "upload";
                }

                studentService.addStudent(name, email, group);
                successCount++;
            }

            model.addAttribute("message",
                    String.format("File processed successfully. Added %d students", successCount));
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

    private String prepareErrorModel(Long groupId, Model model) {
        model.addAttribute("students", studentService.getStudentsByGroup(groupId));
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("selectedGroupId", groupId);
        model.addAttribute("sort", "name");
        return "students";
    }
}