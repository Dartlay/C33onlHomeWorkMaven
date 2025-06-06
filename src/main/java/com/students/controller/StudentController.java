package com.students.controller;


import com.students.model.Student;
import com.students.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", service.getAll());
        return "list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("student", new Student());
        return "add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Student student) {
        service.addStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.removeStudent(id);
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Student student = service.getAll().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (student == null) return "redirect:/students";

        model.addAttribute("student", student);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Student student) {
        service.updateStudent(id, student);
        return "redirect:/students";
    }
}
