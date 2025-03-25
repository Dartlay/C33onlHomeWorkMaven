package org.time.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String city = req.getPathInfo().substring(1);
        ZoneId zoneId = getZoneForCity(city);

        if (zoneId != null) {
            String time = ZonedDateTime.now(zoneId)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

            out.println("<html><body>");
            out.println("<h1>Time in " + city + "</h1>");
            out.println("<p>" + time + "</p>");
            out.println("<p><a href='/'>Back</a></p>");
            out.println("</body></html>");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "City not found");
        }
    }

    private ZoneId getZoneForCity(String city) {
        switch (city.toLowerCase()) {
            case "minsk": return ZoneId.of("Europe/Minsk");
            case "washington": return ZoneId.of("America/New_York");
            case "beijing": return ZoneId.of("Asia/Shanghai");
            default: return null;
        }
    }
}