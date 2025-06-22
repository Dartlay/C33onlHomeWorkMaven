package com.students.service;

import com.students.model.Group;
import com.students.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceTest.class);
    private StudentService studentService;
    private Group testGroup;

    @BeforeEach
    void setUp() {
        logger.info("Initializing StudentService for test");
        studentService = new StudentService();
        testGroup = new Group(1, "TestGroup");
    }

    @Test
    void getAllStudents_shouldReturnEmptyListInitially() {
        logger.debug("Testing getAllStudents() with empty list");
        List<Student> students = studentService.getAllStudents();
        assertNotNull(students);
        assertTrue(students.isEmpty());
    }

    @Test
    void addStudent_shouldIncrementId() {
        logger.debug("Testing addStudent()");
        studentService.addStudent("John Doe", "john@example.com", testGroup);
        studentService.addStudent("Jane Smith", "jane@example.com", testGroup);

        List<Student> students = studentService.getAllStudents();
        assertEquals(2, students.size());
        assertEquals(1, students.get(0).getId());
        assertEquals(2, students.get(1).getId());
    }

    @Test
    void getStudentsByGroup_shouldFilterCorrectly() {
        logger.debug("Testing getStudentsByGroup()");
        Group anotherGroup = new Group(2, "AnotherGroup");

        studentService.addStudent("Student1", "s1@example.com", testGroup);
        studentService.addStudent("Student2", "s2@example.com", anotherGroup);
        studentService.addStudent("Student3", "s3@example.com", testGroup);

        List<Student> filtered = studentService.getStudentsByGroup(testGroup.getId());
        assertEquals(2, filtered.size());
        assertTrue(filtered.stream().allMatch(s -> s.getGroup().getId() == testGroup.getId()));
    }

    @Test
    void removeStudent_shouldDeleteCorrectStudent() {
        logger.debug("Testing removeStudent()");
        studentService.addStudent("ToDelete", "delete@example.com", testGroup);
        int initialSize = studentService.getAllStudents().size();

        studentService.removeStudent(1);
        assertEquals(initialSize - 1, studentService.getAllStudents().size());
        assertTrue(studentService.getAllStudents().stream().noneMatch(s -> s.getId() == 1));
    }

    @Test
    void findAll_shouldReturnAllStudents() {
        logger.debug("Testing findAll()");
        studentService.addStudent("StudentA", "a@example.com", testGroup);
        studentService.addStudent("StudentB", "b@example.com", testGroup);

        List<Student> students = studentService.findAll();
        assertEquals(2, students.size());
    }
}