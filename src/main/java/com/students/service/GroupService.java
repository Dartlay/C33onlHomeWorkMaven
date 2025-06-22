package com.students.service;

import com.students.model.Group;
import com.students.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(int id) {
        return groupRepository.findById(id).orElse(null);
    }

    public Group getGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    public void addGroup(String name) {
        groupRepository.save(new Group(0, name));
    }

    public void deleteGroup(int id) {
        groupRepository.deleteById(id);
    }
}