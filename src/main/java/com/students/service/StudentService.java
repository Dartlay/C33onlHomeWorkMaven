package com.students.service;

import com.students.model.Group;
import com.students.model.Student;
import com.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(String name, String email, Group group) {
        studentRepository.save(new Student(0, name, email, group));
    }

    public List<Student> getStudentsByGroup(int groupId) {
        return studentRepository.findByGroupId(groupId);
    }

    public void removeStudent(int id) {
        studentRepository.deleteById(id);
    }

    public void updateStudentEmail(int id, String email) {
        studentRepository.updateEmail(id, email);
    }

    public List<Student> searchStudents(String name) {
        return studentRepository.searchByName(name);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }
}