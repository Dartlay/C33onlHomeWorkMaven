package com.students.controller;

import com.students.model.Group;
import com.students.model.Student;
import com.students.service.GroupService;
import com.students.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    private final StudentService studentService;
    private final GroupService groupService;

    public StudentRestController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.findAll();
        return ResponseEntity.ok(students);
    }

    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody StudentDto studentDto) {
        Group group = groupService.getGroupById(studentDto.getGroupId());
        if (group == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group not found");
        }

        studentService.addStudent(studentDto.getName(), studentDto.getEmail(), group);
        return ResponseEntity.status(HttpStatus.CREATED).body("Student added");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok("Student deleted");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadStudentsFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file selected");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            int lineNum = 0;

            while ((line = reader.readLine()) != null) {
                lineNum++;
                String[] data = line.split(",");

                if (data.length != 3) {
                    return ResponseEntity.badRequest()
                            .body("Error on line " + lineNum + ": format must be 'name,email,group'");
                }

                String name = data[0].trim();
                String email = data[1].trim();
                String groupName = data[2].trim();

                if (name.isEmpty() || !email.contains("@")) {
                    return ResponseEntity.badRequest()
                            .body("Validation error on line " + lineNum + ": empty name or invalid email");
                }

                Group group = groupService.getAllGroups().stream()
                        .filter(g -> g.getName().equalsIgnoreCase(groupName))
                        .findFirst()
                        .orElse(null);

                if (group == null) {
                    return ResponseEntity.badRequest()
                            .body("Error: group '" + groupName + "' not found (line " + lineNum + ")");
                }

                studentService.addStudent(name, email, group);
            }

            return ResponseEntity.ok("File processed successfully");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File read error: " + e.getMessage());
        }
    }
}