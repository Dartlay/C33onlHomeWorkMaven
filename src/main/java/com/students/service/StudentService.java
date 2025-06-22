package com.students.service;

import com.students.model.Group;
import com.students.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final List<Student> students = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);


    public List<Student> getAllStudents() {
        logger.trace("Entering getAllStudents");
        logger.debug("Getting all students, count: {}", students.size());
        return students;
    }

    public void addStudent(String name, String email, Group group) {
        logger.trace("Entering addStudent with params: name={}, email={}, group={}",
                name, email, group != null ? group.getName() : "null");

        logger.info("Attempting to add student: name={}, email={}, group={}", name, email, group.getName());
        try {
            Student student = new Student(idCounter.getAndIncrement(), name, email, group);
            students.add(student);
            logger.info("Successfully added student: id={}, name={}", student.getId(), student.getName());
        } catch (Exception e) {
            logger.error("Failed to add student: name={}", name, e);
            throw e;
        }
    }

    public List<Student> getStudentsByGroup(int groupId) {
        logger.trace("Entering getStudentsByGroup with groupId={}", groupId);
        logger.debug("Getting students by group id: {}", groupId);
        return students.stream()
                .filter(s -> s.getGroup().getId() == groupId)
                .collect(Collectors.toList());
    }

    public void removeStudent(int id) {
        logger.trace("Entering removeStudent with id={}", id);
        logger.info("Attempting to remove student with id: {}", id);
        boolean removed = students.removeIf(s -> s.getId() == id);
        if (removed) {
            logger.info("Successfully removed student with id: {}", id);
        } else {
            logger.warn("No student found with id: {}", id);
        }
    }

    public List<Student> findAll() {
        logger.trace("Entering findAll");
        logger.debug("Finding all students");
        return new ArrayList<>(students);
    }
}
