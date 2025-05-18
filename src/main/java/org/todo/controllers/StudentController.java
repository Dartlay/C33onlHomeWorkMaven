package org.todo.controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.todo.dao.StudentDao;
import org.todo.models.Student;
import java.io.IOException;

@WebServlet("/students")
public class StudentController extends HttpServlet {
    private StudentDao studentDao = new StudentDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/student/add.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Student student = studentDao.findById(id);
            request.setAttribute("student", student);
            request.getRequestDispatcher("/WEB-INF/views/student/edit.jsp").forward(request, response);
        } else {
            request.setAttribute("students", studentDao.findAll());
            request.getRequestDispatcher("/WEB-INF/views/student/list.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            Student student = new Student();
            student.setName(request.getParameter("name"));
            student.setGroup(request.getParameter("group"));
            studentDao.save(student);
        } else if ("edit".equals(action)) {
            Student student = new Student();
            student.setId(Integer.parseInt(request.getParameter("id")));
            student.setName(request.getParameter("name"));
            student.setGroup(request.getParameter("group"));
            studentDao.update(student);
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            studentDao.delete(id);
        }
        response.sendRedirect(request.getContextPath() + "/students");
    }
}