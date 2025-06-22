package com.students.controller;

import com.students.dto.StudentExportDto;
import com.students.model.Group;
import com.students.model.Student;
import com.students.service.GroupService;
import com.students.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/students")
@Tag(name = "Students", description = "Управление студентами и их загрузкой из файлов")
public class StudentController {

    private final StudentService studentService;
    private final GroupService groupService;

    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    @Operation(summary = "Просмотр студентов по группе", description = "Отображает список студентов с сортировкой")
    @ApiResponse(responseCode = "200", description = "Успешно загружен список студентов")
    public String getStudentsByGroup(
            @Parameter(description = "ID группы", example = "1")
            @RequestParam(required = false, defaultValue = "1") int groupId,

            @Parameter(description = "Сортировка: name, email, id", example = "name")
            @RequestParam(required = false, defaultValue = "name") String sort,

            Model model
    ) {
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
    @Operation(summary = "Добавить студента", description = "Добавляет нового студента в выбранную группу")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Редирект на список студентов"),
            @ApiResponse(responseCode = "200", description = "Ошибка валидации — имя не в латинице")
    })
    public String addStudent(
            @Parameter(description = "Имя студента (только латиница)", required = true)
            @RequestParam String name,

            @Parameter(description = "Email студента", required = true)
            @RequestParam String email,

            @Parameter(description = "ID группы", required = true)
            @RequestParam int groupId,

            Model model
    ) {
        if (!name.matches("^[a-zA-Z\\s'-]+$")) {
            model.addAttribute("nameError", "Name must contain only Latin letters, spaces, " +
                    "apostrophes or hyphens");
            List<Student> students = studentService.getStudentsByGroup(groupId);
            model.addAttribute("students", students);
            model.addAttribute("groups", groupService.getAllGroups());
            model.addAttribute("selectedGroupId", groupId);
            model.addAttribute("sort", "name");
            return "students";
        }

        Group group = groupService.getGroupById(groupId);
        studentService.addStudent(name, email, group);
        return "redirect:/students?groupId=" + groupId;
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "Удалить студента", description = "Удаляет студента по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Редирект на обновлённый список студентов")
    })
    public String deleteStudent(
            @Parameter(description = "ID студента", required = true)
            @PathVariable int id,

            @RequestParam(required = false, defaultValue = "1") int groupId
    ) {
        studentService.removeStudent(id);
        return "redirect:/students?groupId=" + groupId;
    }

    @GetMapping("/upload")
    @Operation(summary = "Страница загрузки", description = "Возвращает HTML-страницу загрузки студентов")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    @Operation(summary = "Обработка TXT файла", description = "Загружает студентов из текстового файла")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Файл успешно загружен"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или формате файла")
    })
    public String handleFileUpload(
            @Parameter(description = "TXT файл со студентами", required = true)
            @RequestParam("file") MultipartFile file,
            Model model
    ) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Файл не выбран.");
            return "upload";
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                String[] data = line.split(",");

                if (data.length != 3) {
                    model.addAttribute("message", "Ошибка формата: строка " + lineNum +
                            " должна содержать name, email, group");
                    return "upload";
                }

                String name = data[0].trim();
                String email = data[1].trim();
                String groupName = data[2].trim();

                if (!name.matches("^[a-zA-Z\\s]+$")) {
                    model.addAttribute("message", "Ошибка валидации имени (строка "
                            + lineNum + ")");
                    return "upload";
                }

                if (email.isEmpty() || !email.contains("@")) {
                    model.addAttribute("message", "Неверный email (строка " + lineNum + ")");
                    return "upload";
                }

                Group group = groupService.getAllGroups().stream()
                        .filter(g -> g.getName().equalsIgnoreCase(groupName))
                        .findFirst()
                        .orElse(null);

                if (group == null) {
                    model.addAttribute("message", "Группа не найдена: " + groupName +
                            " (строка " + lineNum + ")");
                    return "upload";
                }

                studentService.addStudent(name, email, group);
            }

        } catch (IOException e) {
            model.addAttribute("message", "Ошибка чтения файла: " + e.getMessage());
            return "upload";
        }

        model.addAttribute("message", "Файл успешно загружен, студенты добавлены.");
        return "upload";
    }

    @GetMapping("/download")
    @Operation(summary = "Скачать студентов", description = "Скачивает список всех студентов в TXT формате")
    public void downloadStudents(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=students.txt");

        try (PrintWriter writer = response.getWriter()) {
            for (Student s : studentService.findAll()) {
                String groupName = s.getGroup() != null ? s.getGroup().getName() : "N/A";
                writer.printf("%s, %s, %s%n", s.getName(), s.getEmail(), groupName);
            }
        }
    }
}
