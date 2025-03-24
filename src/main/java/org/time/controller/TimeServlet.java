package org.time.controller;

import org.time.service.TimeService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TimeServlet extends HttpServlet {
    @Inject
    private TimeService timeService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String pathInfo = req.getPathInfo();
        if (req.getSession().getAttribute("ageVerified") == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Age verification required");
            return;
        }
        if (pathInfo != null) {
            String city = pathInfo.substring(1);
            resp.getWriter().write(timeService.getCurrentTime(city));
        } else {
            resp.getWriter().write("Please specify a city (e.g., /minsk)");
        }
    }
}