package com.students.controller;

import com.students.model.Group;
import com.students.model.Student;
import com.students.service.GroupService;
import com.students.service.StudentService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class StudentRestController {
    private final StudentService studentService;
    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody StudentDto studentDto) {
        Group group = groupService.getGroupById(studentDto.getGroupId());
        if (group == null) {
            return ResponseEntity.badRequest().body("Group not found");
        }

        try {
            studentService.addStudent(studentDto.getName(), studentDto.getEmail(), group);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating student");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadStudentsFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            int lineNumber = 0;
            int successCount = 0;
            StringBuilder errors = new StringBuilder();

            while (reader.ready()) {
                lineNumber++;
                String line = reader.readLine();
                String[] data = line.split(",");

                if (data.length != 3) {
                    errors.append("Line ").append(lineNumber).append(": Invalid format. Expected name,email,group\n");
                    continue;
                }

                String name = data[0].trim();
                String email = data[1].trim();
                String groupName = data[2].trim();

                if (name.isEmpty() || !email.contains("@")) {
                    errors.append("Line ").append(lineNumber).append(": Invalid name or email\n");
                    continue;
                }

                Group group = groupService.getAllGroups().stream()
                        .filter(g -> g.getName().equalsIgnoreCase(groupName))
                        .findFirst()
                        .orElse(null);

                if (group == null) {
                    errors.append("Line ").append(lineNumber).append(": Group not found - ").append(groupName).append("\n");
                    continue;
                }

                studentService.addStudent(name, email, group);
                successCount++;
            }

            String message = String.format("Added %d students", successCount);
            if (errors.length() > 0) {
                message += "\nErrors:\n" + errors;
            }

            return ResponseEntity.ok(message);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error processing file");
        }
    }

    public static class StudentDto {
        private String name;
        private String email;
        private Long groupId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }
    }
}