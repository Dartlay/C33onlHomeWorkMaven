package com.example.servlet;

import com.example.model.Book;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


@WebServlet("/load-book")
@MultipartConfig
public class LoadBookServlet extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Получаем данные формы
        Part filePart = request.getPart("book");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String description = request.getParameter("description");
        int year = Integer.parseInt(request.getParameter("year"));
        String language = request.getParameter("language");

        // Сохраняем файл
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String booksDir = getServletContext().getRealPath("/books");
        Files.createDirectories(Paths.get(booksDir));
        File bookFile = new File(booksDir, fileName);

        try (InputStream fileContent = filePart.getInputStream();
             FileOutputStream out = new FileOutputStream(bookFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileContent.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }


        Book book = new Book();
        book.setTitle(title);
        book.setFilePath("/books/" + fileName);
        book.setUploadedBy((Integer) request.getSession().getAttribute("userId"));
        book.setAuthor(author);
        book.setDescription(description);
        book.setPublicationYear(year);
        book.setLanguage(language);


        }
    }
