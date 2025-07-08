package com.students.dao;

import com.students.model.Group;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.*;

import java.util.List;

@Repository
@Transactional
public class GroupDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public GroupDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Group save(Group group) {
        getCurrentSession().persist(group);
        return group;
    }

    public List<Group> findAll() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Group> cq = cb.createQuery(Group.class);
        Root<Group> root = cq.from(Group.class);
        cq.select(root);
        return getCurrentSession().createQuery(cq).getResultList();
    }

    public Group findById(Long id) {
        return getCurrentSession().get(Group.class, id);
    }

    public Group findByName(String name) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Group> cq = cb.createQuery(Group.class);
        Root<Group> root = cq.from(Group.class);
        cq.where(cb.equal(root.get("name"), name));
        return getCurrentSession().createQuery(cq).uniqueResult();
    }

    public void delete(Long id) {
        Group group = getCurrentSession().get(Group.class, id);
        if (group != null) {
            getCurrentSession().remove(group);
        }
    }
}