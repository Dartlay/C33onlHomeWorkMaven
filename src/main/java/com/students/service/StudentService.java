package com.students.service;


import com.students.exception.DuplicateEmailException;
import com.students.model.Group;
import com.students.model.Student;
import com.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupService groupService;


    @Transactional
    public Student addStudent(String name, String surname, String email,
                              boolean hasPaid, Long groupId) {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found with id: " + groupId);
        }
        if (studentRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email '" + email + "' is already registered");
        }
        Student student = Student.builder()
                .name(name)
                .surname(surname)
                .email(email)
                .hasPaid(hasPaid)
                .group(group)
                .build();

        try {
            return studentRepository.save(student);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException("Email '" + email + "' is already registered", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsByGroup(Long groupId) {
        return studentRepository.findByGroupId(groupId);
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsWithUnpaidCourses() {
        return studentRepository.findByHasPaidFalse();
    }

    @Transactional(readOnly = true)
    public Student getStudentByName(String name) {
        return studentRepository.findByName(name).orElse(null);
    }

    @Transactional(readOnly = true)
    public Student getStudentBySurname(String surname) {
        return studentRepository.findBySurname(surname).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsByGroupName(String groupName) {
        return studentRepository.findByGroupName(groupName);
    }

    @Transactional
    public Student updateStudent(Long id, String name, String surname, String email,
                                 Boolean hasPaid, Long groupId) {
        Student student = studentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Student not found"));

        if (name != null) student.setName(name);
        if (surname != null) student.setSurname(surname);
        if (email != null) student.setEmail(email);
        if (hasPaid != null) student.setHasPaid(hasPaid);
        if (groupId != null) {
            Group group = groupService.getGroupById(groupId);
            if (group != null) {
                student.setGroup(group);
            }
        }

        return studentRepository.save(student);
    }

    @Transactional
    public void removeStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    public void transferStudentToAnotherGroup(Long studentId, Long newGroupId) {
        Group newGroup = groupService.getGroupById(newGroupId);
        if (newGroup == null) {
            throw new IllegalArgumentException("New group not found");
        }

        studentRepository.changeStudentGroup(studentId, newGroupId);
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
}