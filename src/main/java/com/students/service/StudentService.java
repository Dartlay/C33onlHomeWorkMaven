package com.students.service;

import com.students.dao.StudentDao;
import com.students.model.Group;
import com.students.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentDao studentDao;
    private final GroupService groupService;

    @Transactional
    public void addStudent(String name, String email, Group group) {
        Student student = Student.builder()
                .name(name)
                .email(email)
                .group(group)
                .build();
        studentDao.save(student);
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsByGroup(Long groupId) {
        return studentDao.findByGroupId(groupId);
    }

    @Transactional
    public void removeStudent(Long id) {
        studentDao.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentDao.findAll();
    }
}