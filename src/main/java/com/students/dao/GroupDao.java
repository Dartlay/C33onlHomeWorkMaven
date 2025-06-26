package com.students.dao;

import com.students.model.Group;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GroupDao {
    private final SessionFactory sessionFactory;

    public Group save(Group group) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(group);
        return group;
    }

    public List<Group> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Group", Group.class).list();
    }

    public Group findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Group.class, id);
    }

    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Group group = session.get(Group.class, id);
        if (group != null) {
            session.remove(group);
        }
    }
}