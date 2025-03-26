package com.example.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.nio.file.Paths;

@WebServlet("/download-book")
public class DownloadBookServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fileName = request.getParameter("file");
        if (fileName == null || fileName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File name not specified");
            return;
        }

        // Защита от directory traversal
        String safeFileName = Paths.get(fileName).getFileName().toString();

        String booksDir = getServletContext().getRealPath("/books");
        File bookFile = new File(booksDir, safeFileName);

        if (!bookFile.exists() || !bookFile.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
            return;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + safeFileName + "\"");

        try (InputStream in = new FileInputStream(bookFile);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}