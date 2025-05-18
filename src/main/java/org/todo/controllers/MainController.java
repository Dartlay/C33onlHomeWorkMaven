package org.todo.controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.todo.dao.GradeDao;
import org.todo.dao.StudentDao;
import org.todo.dao.SubjectDao;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@WebServlet("/")
public class MainController extends HttpServlet {
    private final StudentDao studentDao = new StudentDao();
    private final SubjectDao subjectDao = new SubjectDao();
    private final GradeDao gradeDao = new GradeDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Loading data for dashboard...");
        System.out.println("Students: " + studentDao.findAll().size());
        System.out.println("Subjects: " + subjectDao.findAll().size());
        System.out.println("Grades: " + gradeDao.findAll().size());


        request.setAttribute("lastUpdate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        request.setAttribute("studentCount", studentDao.findAll().size());
        request.setAttribute("studentGroups", studentDao.getGroupCount());
        request.setAttribute("subjectCount", subjectDao.findAll().size());
        request.setAttribute("gradeCount", gradeDao.findAll().size());
        request.setAttribute("gradeAverage", gradeDao.getAverageGrade());
        request.setAttribute("gradeMax", gradeDao.getMaxGrade());
        request.setAttribute("gradeMin", gradeDao.getMinGrade());
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}