package com.students.dao;

import com.students.model.Student;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudentDao {
    private final SessionFactory sessionFactory;

    public Student save(Student student) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(student);
        return student;
    }

    public List<Student> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Student", Student.class).list();
    }

    public List<Student> findByGroupId(Long groupId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "from Student where group.id = :groupId", Student.class)
                .setParameter("groupId", groupId)
                .list();
    }

    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Student student = session.get(Student.class, id);
        if (student != null) {
            session.remove(student);
        }
    }
}