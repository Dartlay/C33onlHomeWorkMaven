package com.students.service;

import com.students.model.Group;
import com.students.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    @Transactional(readOnly = true)
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Group getGroupById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    @Transactional
    public Group addGroup(String name) {
        Group group = Group.builder().name(name).build();
        return groupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Group getGroupByName(String name) {
        return groupRepository.findByName(name).orElse(null);
    }
}