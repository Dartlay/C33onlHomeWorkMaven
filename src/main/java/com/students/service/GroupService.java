package com.students.service;

import com.students.dao.GroupDao;
import com.students.model.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupDao groupDao;

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
        Group group = Group.builder().name(name).build();
        return groupDao.save(group);
    }

    @Transactional
    public void deleteGroup(Long id) {
        groupDao.delete(id);
    }
}