package com.request;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/save-request")
public class SaveRequestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                message == null || message.trim().isEmpty()) {
            request.setAttribute("error", "Все поля обязательны!");
            request.getRequestDispatcher("save-request.jsp").forward(request, response);
            return;
        }
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("message", message);

        request.getRequestDispatcher("success.jsp").forward(request, response);
    }
}