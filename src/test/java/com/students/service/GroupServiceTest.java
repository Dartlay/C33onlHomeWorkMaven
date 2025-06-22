package com.students.service;

import com.students.model.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(GroupServiceTest.class);
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        logger.info("Initializing GroupService for test");
        groupService = new GroupService();
    }

    @Test
    void getAllGroups_shouldReturnAllGroups() {
        logger.debug("Testing getAllGroups()");
        List<Group> groups = groupService.getAllGroups();
        assertNotNull(groups);
        assertEquals(2, groups.size());
        assertEquals("C33Java", groups.get(0).getName());
        assertEquals("C34Python", groups.get(1).getName());
    }

    @Test
    void getGroupById_shouldReturnCorrectGroup() {
        logger.debug("Testing getGroupById() with existing ID");
        Group group = groupService.getGroupById(1);
        assertNotNull(group);
        assertEquals(1, group.getId());
        assertEquals("C33Java", group.getName());

        logger.debug("Testing getGroupById() with non-existing ID");
        assertNull(groupService.getGroupById(999));
    }

    @Test
    void deleteGroup_shouldRemoveGroup() {
        logger.debug("Testing deleteGroup()");
        int initialSize = groupService.getAllGroups().size();
        groupService.deleteGroup(1);
        assertEquals(initialSize - 1, groupService.getAllGroups().size());
        assertNull(groupService.getGroupById(1));
    }

    @Test
    void getGroupByName_shouldFindGroup() {
        logger.debug("Testing getGroupByName() with existing name");
        Group group = groupService.getGroupByName("c33java");
        assertNotNull(group);
        assertEquals("C33Java", group.getName());

        logger.debug("Testing getGroupByName() with non-existing name");
        assertNull(groupService.getGroupByName("NonExistingGroup"));
    }

    @Test
    void addGroup_shouldIncrementId() {
        logger.debug("Testing addGroup()");
        int initialSize = groupService.getAllGroups().size();
        groupService.addGroup("NewGroup");
        assertEquals(initialSize + 1, groupService.getAllGroups().size());
        assertEquals(3, groupService.getGroupById(3).getId());
        assertEquals("NewGroup", groupService.getGroupById(3).getName());
    }
}