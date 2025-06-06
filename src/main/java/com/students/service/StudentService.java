package com.students.service;


import com.students.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class StudentService {
    private final List<Student> students = new ArrayList<>();
    private long nextId = 1;

    public List<Student> getAll() {
        return students;
    }

    public void addStudent(Student student) {
        student.setId(nextId++);
        students.add(student);
    }

    public void updateStudent(Long id, Student updated) {
        for (Student s : students) {
            if (s.getId().equals(id)) {
                s.setFirstName(updated.getFirstName());
                s.setLastName(updated.getLastName());
                return;
            }
        }
    }

    public void removeStudent(Long id) {
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId().equals(id)) {
                iterator.remove();
                return;
            }
        }
    }
}

