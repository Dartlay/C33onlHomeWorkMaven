package com.example.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.List;

public class FileUploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory())
                        .parseRequest(request);

                for (FileItem item : items) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String path = "/usr/local/tomcat/webapps/ROOT/books/" + fileName;

                        // Create directory if not exists
                        new File("/usr/local/tomcat/webapps/ROOT/books").mkdirs();

                        item.write(new File(path));
                    }
                }
            } catch (Exception e) {
                throw new ServletException("Upload failed", e);
            }
        }
        response.sendRedirect(request.getContextPath() + "/");
    }
}