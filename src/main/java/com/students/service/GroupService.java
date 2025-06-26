package com.students.service;

import com.students.dao.GroupDao;
import com.students.model.Group;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupDao groupDao;

    @PostConstruct
    @Transactional
    public void initGroups() {
        if (groupDao.findAll().isEmpty()) {
            groupDao.save(new Group(null, "C33Java", null));
            groupDao.save(new Group(null, "C34Python", null));
        }
    }

    @Transactional(readOnly = true)
    public List<Group> getAllGroups() {
        return groupDao.findAll();
    }

    @Transactional(readOnly = true)
    public Group getGroupById(Long id) {
        return groupDao.findById(id);
    }

    @Transactional
    public Group addGroup(String name) {
        return groupDao.save(new Group(null, name, null));
    }

    @Transactional
    public void deleteGroup(Long id) {
        groupDao.delete(id);
    }
}