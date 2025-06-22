package com.students.service;

import com.students.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
    private final Map<Integer, Group> groups = new LinkedHashMap<>();
    private int currentId = 1;

    public GroupService() {
        logger.info("Initializing GroupService with sample data");
        addGroup("C33Java");
        addGroup("C34Python");
    }

    public List<Group> getAllGroups() {
        logger.debug("Fetching all groups, current count: {}", groups.size());
        return new ArrayList<>(groups.values());
    }

    public Group getGroupById(int id) {
        logger.debug("Looking for group with ID: {}", id);
        Group group = groups.get(id);
        if (group == null) {
            logger.warn("Group with ID {} not found", id);
        }
        return group;
    }

    public void deleteGroup(int id) {
        logger.info("Attempting to delete group with ID: {}", id);
        Group removed = groups.remove(id);
        if (removed != null) {
            logger.info("Deleted group: ID={}, Name={}", removed.getId(), removed.getName());
        } else {
            logger.warn("No group found with ID: {} to delete", id);
        }
    }

    public Group getGroupByName(String name) {
        logger.debug("Searching for group by name: {}", name);
        return groups.values().stream()
                .filter(g -> g.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void addGroup(String name) {
        logger.info("Adding new group: Name={}", name);
        Group newGroup = new Group(currentId, name);
        groups.put(currentId, newGroup);
        logger.debug("Group added: ID={}, Name={}", currentId, name);
        currentId++;
    }
}