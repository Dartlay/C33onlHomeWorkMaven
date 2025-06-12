package com.students.controller;

import com.students.model.Group;
import com.students.model.Student;
import com.students.service.GroupService;
import com.students.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final GroupService groupService;

    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public String getStudentsByGroup(@RequestParam(required = false, defaultValue = "1") int groupId,
                                     @RequestParam(required = false, defaultValue = "name") String sort,
                                     Model model) {

        List<Student> students = studentService.getStudentsByGroup(groupId);

        Comparator<Student> comparator = switch (sort) {
            case "email" -> Comparator.comparing(Student::getEmail);
            case "name" -> Comparator.comparing(Student::getName);
            default -> Comparator.comparing(Student::getId);
        };

        students.sort(comparator);

        model.addAttribute("students", students);
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("selectedGroupId", groupId);
        model.addAttribute("sort", sort);

        return "students";
    }

    @PostMapping("/add")
    public String addStudent(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam int groupId,
                             Model model) {
        if (!name.matches("^[a-zA-Z\\s'-]+$")) {
            model.addAttribute("nameError", "Name must contain only Latin letters," +
                    "spaces, apostrophes or hyphens");
            List<Student> students = studentService.getStudentsByGroup(groupId);
            model.addAttribute("students", students);
            model.addAttribute("groups", groupService.getAllGroups());
            model.addAttribute("selectedGroupId", groupId);
            model.addAttribute("sort", "name");

            return "students";
        }

        Group group = groupService.getGroupById(groupId);
        studentService.addStudent(name, email, group);

        return "redirect:/students?groupId=" + groupId;
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id,
                                @RequestParam(required = false, defaultValue = "1") int groupId) {
        studentService.removeStudent(id);
        return "redirect:/students?groupId=" + groupId;
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "File not selected It has Latin in it");
            return "upload";
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                String[] data = line.split(",");

                if (data.length != 3) {
                    model.addAttribute("message", "Format error: line "
                            + lineNum + " must contain name, email, and group");
                    return "upload";
                }

                String name = data[0].trim();
                String email = data[1].trim();
                String groupName = data[2].trim();

                if (!name.matches("^[a-zA-Z\\s]+$")) {
                    model.addAttribute("message", "Validation error: name must contain only " +
                            "Latin letters (line " + lineNum + ")");
                    return "upload";
                }

                if (email.isEmpty() || !email.contains("@")) {
                    model.addAttribute("message", "Validation error: invalid email " +
                            "(line " + lineNum + ")");
                    return "upload";
                }

                Group group = groupService.getAllGroups().stream()
                        .filter(g -> g.getName().equalsIgnoreCase(groupName))
                        .findFirst()
                        .orElse(null);

                if (group == null) {
                    model.addAttribute("message", "Group not found: " + groupName + "" +
                            " (line " + lineNum + ")");
                    return "upload";
                }

                studentService.addStudent(name, email, group);
            }

        } catch (IOException e) {
            model.addAttribute("message", "File read error: " + e.getMessage());
            return "upload";
        }

        model.addAttribute("message", "File successfully uploaded and students added.");
        return "upload";
    }


    @GetMapping("/download")
    public void downloadStudents(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=students.txt");

        try (PrintWriter writer = response.getWriter()) {
            for (Student s : studentService.findAll()) {
                writer.printf("%s, %s, %s%n", s.getName(), s.getEmail(), s.getGroup().getName());
            }
        }
    }
}

