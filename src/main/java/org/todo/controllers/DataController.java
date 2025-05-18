package org.todo.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.todo.dao.GradeDao;
import org.todo.dao.StudentDao;
import org.todo.dao.SubjectDao;
import org.todo.models.Student;
import org.todo.models.Subject;
import org.todo.models.Grade;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

@WebServlet("/data")
@MultipartConfig
public class DataController extends HttpServlet {
    private final StudentDao studentDao = new StudentDao();
    private final SubjectDao subjectDao = new SubjectDao();
    private final GradeDao gradeDao = new GradeDao();


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String format = request.getParameter("format");
        if ("export".equals(action)) {
            switch (format) {
                case "txt":
                    exportTxt(response);
                    break;
                case "json":
                default:
                    exportJson(response);
            }
        }
    }

    private void exportJson(HttpServletResponse response) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("students", studentDao.findAll());
        data.put("subjects", subjectDao.findAll());
        data.put("grades", gradeDao.findAll());
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=\"student_data.json\"");
        Gson gson = new Gson();
        gson.toJson(data, response.getWriter());
    }

    private void exportTxt(HttpServletResponse response) throws IOException {
        List<Student> students = studentDao.findAll();
        List<Subject> subjects = subjectDao.findAll();
        List<Grade> grades = gradeDao.findAll();
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=\"student_data.txt\"");
        PrintWriter writer = response.getWriter();
        writer.println("=== STUDENT MANAGEMENT DATA EXPORT ===");
        writer.println("Generated: " + new Date());
        writer.println();
        writer.println("===== STUDENTS (" + students.size() + ") =====");
        students.forEach(s -> writer.printf("ID: %d | Name: %-20s | Group: %s%n",
                s.getId(), s.getName(), s.getGroup()));
        writer.println("\n===== SUBJECTS (" + subjects.size() + ") =====");
        subjects.forEach(s -> writer.printf("ID: %d | Name: %s%n", s.getId(), s.getName()));
        writer.println("\n===== GRADES (" + grades.size() + ") =====");
        grades.forEach(g -> {
            Student s = studentDao.findById(g.getStudentId());
            Subject sub = subjectDao.findById(g.getSubjectId());
            writer.printf("Student: %-20s | Group: %-10s | Subject: %-15s | Grade: %d | Date: %s%n",
                    s != null ? s.getName() : "N/A",
                    s != null ? s.getGroup() : "N/A",
                    sub != null ? sub.getName() : "N/A",
                    g.getValue(),
                    g.getDate());
        });

        writer.println("\n=== END OF EXPORT ===");
    }
}
