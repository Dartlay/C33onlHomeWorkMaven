package com.students.service;


import com.students.model.Group;
import com.students.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final List<Student> students = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public List<Student> getAllStudents() {
        return students;
    }

    public void addStudent(String name, String email, Group group) {
        students.add(new Student(idCounter.getAndIncrement(), name, email, group));
    }

    public List<Student> getStudentsByGroup(int groupId) {
        return students.stream()
                .filter(s -> s.getGroup().getId() == groupId)
                .collect(Collectors.toList());
    }

    public void removeStudent(int id) {
        students.removeIf(s -> s.getId() == id);
    }
}
