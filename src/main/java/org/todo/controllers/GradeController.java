package org.todo.controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.todo.dao.*;
import org.todo.models.Grade;
import java.io.IOException;

@WebServlet("/grades")
public class GradeController extends HttpServlet {
    private final GradeDao gradeDao = new GradeDao();
    private final StudentDao studentDao = new StudentDao();
    private final SubjectDao subjectDao = new SubjectDao();
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            request.setAttribute("students", studentDao.findAll());
            request.setAttribute("subjects", subjectDao.findAll());
            request.getRequestDispatcher("/WEB-INF/views/grade/add.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Grade grade = gradeDao.findById(id);
            if (grade == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Grade not found");
                return;
            }
            request.setAttribute("grade", grade);
            request.setAttribute("students", studentDao.findAll());
            request.setAttribute("subjects", subjectDao.findAll());
            request.getRequestDispatcher("/WEB-INF/views/grade/edit.jsp").forward(request, response);
        } else {
            request.setAttribute("grades", gradeDao.findAllWithDetails());
            request.getRequestDispatcher("/WEB-INF/views/grade/list.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("edit".equals(action)) {
                Grade grade = new Grade();
                grade.setId(Integer.parseInt(request.getParameter("id")));
                grade.setStudentId(Integer.parseInt(request.getParameter("studentId")));
                grade.setSubjectId(Integer.parseInt(request.getParameter("subjectId")));
                grade.setValue(Integer.parseInt(request.getParameter("value")));
                grade.setDate(request.getParameter("date"));
                gradeDao.update(grade);
            } else {
                Grade grade = new Grade();
                grade.setStudentId(Integer.parseInt(request.getParameter("studentId")));
                grade.setSubjectId(Integer.parseInt(request.getParameter("subjectId")));
                grade.setValue(Integer.parseInt(request.getParameter("value")));
                gradeDao.save(grade);
            }
            response.sendRedirect(request.getContextPath() + "/grades");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data");
        }
    }
}