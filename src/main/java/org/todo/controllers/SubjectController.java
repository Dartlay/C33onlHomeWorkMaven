package org.todo.controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.todo.dao.SubjectDao;
import org.todo.models.Subject;

import java.io.IOException;

@WebServlet("/subjects")
public class SubjectController extends HttpServlet {
    private SubjectDao subjectDao = new SubjectDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/subjects/add.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Subject subject = subjectDao.findById(id);
            request.setAttribute("subject", subject);
            request.getRequestDispatcher("/WEB-INF/views/subjects/edit.jsp").forward(request, response);
        } else {
            request.setAttribute("subjects", subjectDao.findAll());
            request.getRequestDispatcher("/WEB-INF/views/subjects/list.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            Subject subject = new Subject();
            subject.setName(request.getParameter("name"));
            subjectDao.save(subject);
        } else if ("edit".equals(action)) {
            Subject subject = new Subject();
            subject.setId(Integer.parseInt(request.getParameter("id")));
            subject.setName(request.getParameter("name"));
            subjectDao.update(subject);
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            subjectDao.delete(id);
        }

        response.sendRedirect(request.getContextPath() + "/subjects");
    }
}