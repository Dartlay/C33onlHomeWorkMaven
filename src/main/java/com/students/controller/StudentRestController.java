package com.students.controller;


import com.students.dto.StudentDto;
import com.students.model.Student;
import com.students.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentRestController {
    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Student>> getStudentsByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(studentService.getStudentsByGroup(groupId));
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<Student>> getStudentsWithUnpaidCourses() {
        return ResponseEntity.ok(studentService.getStudentsWithUnpaidCourses());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Student> getStudentByName(@PathVariable String name) {
        Student student = studentService.getStudentByName(name);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }

    @GetMapping("/surname/{surname}")
    public ResponseEntity<Student> getStudentBySurname(@PathVariable String surname) {
        Student student = studentService.getStudentBySurname(surname);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }

    @GetMapping("/group-name/{groupName}")
    public ResponseEntity<List<Student>> getStudentsByGroupName(@PathVariable String groupName) {
        return ResponseEntity.ok(studentService.getStudentsByGroupName(groupName));
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody StudentDto studentDto) {
        Student student = studentService.addStudent(
                studentDto.getName(),
                studentDto.getSurname(),
                studentDto.getEmail(),
                studentDto.isHasPaid(),
                studentDto.getGroupId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentDto studentDto) {
        Student student = studentService.updateStudent(
                id,
                studentDto.getName(),
                studentDto.getSurname(),
                studentDto.getEmail(),
                studentDto.isHasPaid(),
                studentDto.getGroupId()
        );
        return ResponseEntity.ok(student);
    }

    @PostMapping("/{studentId}/transfer/{newGroupId}")
    public ResponseEntity<Void> transferStudent(
            @PathVariable Long studentId,
            @PathVariable Long newGroupId) {
        studentService.transferStudentToAnotherGroup(studentId, newGroupId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
        return ResponseEntity.noContent().build();
    }
}