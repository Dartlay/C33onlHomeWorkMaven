package com.students.controller;


import com.students.model.Group;
import com.students.model.Student;
import com.students.service.GroupService;
import com.students.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final GroupService groupService;

    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public String getStudentsByGroup(@RequestParam(required = false, defaultValue = "1") int groupId,
                                     @RequestParam(required = false, defaultValue = "name") String sort,
                                     Model model) {

        List<Student> students = studentService.getStudentsByGroup(groupId);

        Comparator<Student> comparator = switch (sort) {
            case "email" -> Comparator.comparing(Student::getEmail);
            case "name" -> Comparator.comparing(Student::getName);
            default -> Comparator.comparing(Student::getId);
        };

        students.sort(comparator);

        model.addAttribute("students", students);
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("selectedGroupId", groupId);
        model.addAttribute("sort", sort);

        return "students";
    }

    @PostMapping("/add")
    public String addStudent(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam int groupId) {

        Group group = groupService.getGroupById(groupId);
        studentService.addStudent(name, email, group);

        return "redirect:/students?groupId=" + groupId;
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id,
                                @RequestParam(required = false, defaultValue = "1") int groupId) {
        studentService.removeStudent(id);
        return "redirect:/students?groupId=" + groupId;
    }
}
