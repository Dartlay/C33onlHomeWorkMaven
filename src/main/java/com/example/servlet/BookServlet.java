package com.example.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/book")
public class BookServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String booksDir = getServletContext().getRealPath("/books");
        File dir = new File(booksDir);
        File[] files = dir.listFiles();

        List<String> bookNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    bookNames.add(file.getName());
                }
            }
        }

        request.setAttribute("books", bookNames);
        request.getRequestDispatcher("/WEB-INF/views/book.jsp").forward(request, response);
    }
}