package com.students.dao;

import com.students.model.Group;
import com.students.model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.*;

import java.util.List;

@Repository
@Transactional
public class StudentDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public StudentDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Student save(Student student) {
        getCurrentSession().persist(student);
        return student;
    }

    public List<Student> findAll() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> root = cq.from(Student.class);
        cq.select(root);
        return getCurrentSession().createQuery(cq).getResultList();
    }

    public List<Student> findByGroupId(Long groupId) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> root = cq.from(Student.class);

        Join<Student, Group> groupJoin = root.join("group");
        cq.where(cb.equal(groupJoin.get("id"), groupId));

        return getCurrentSession().createQuery(cq).getResultList();
    }

    public void delete(Long id) {
        Student student = getCurrentSession().get(Student.class, id);
        if (student != null) {
            getCurrentSession().remove(student);
        }
    }
}