package com.example.servlet;

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
        Part filePart = request.getPart("book");
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
        request.setAttribute("fileName", fileName);
        request.getRequestDispatcher("/WEB-INF/views/success.jsp").forward(request, response);
    }
}