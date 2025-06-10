package com.students.service;


import com.students.model.Group;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupService {
    private final Map<Integer, Group> groups = new LinkedHashMap<>();
    private int currentId = 1;

    public GroupService() {

        addGroup("C33Java");
        addGroup("C34Python");
    }

    public List<Group> getAllGroups() {
        return new ArrayList<>(groups.values());
    }

    public Group getGroupById(int id) {
        return groups.get(id);
    }

    public void addGroup(String name) {
        groups.put(currentId, new Group(currentId, name));
        currentId++;
    }
}
