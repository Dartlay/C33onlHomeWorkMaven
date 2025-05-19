package org.example.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrontController extends HttpServlet {
    private Map<String, Command> commands;

    @Override
    public void init() {
        commands = new HashMap<>();
        commands.put("home", new HomeCommand());
        commands.put("about", new AboutCommand());
    }
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        Command command = commands.get(path.substring(1));

        if (command == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String view = command.execute(request, response);
        request.getRequestDispatcher(view).forward(request, response);
    }
}