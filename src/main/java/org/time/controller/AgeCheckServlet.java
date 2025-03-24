package org.time.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/check-age")
public class AgeCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String ageParam = req.getParameter("age");

        try {
            int age = Integer.parseInt(ageParam);

            if (age < 1 || age > 120) {
                sendErrorPage(out, "Age must be between 1 and 120 years");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (age >= 18) {
                // Adult - show success page
                out.println("<html><body>");
                out.println("<h1>Access Granted</h1>");
                out.println("<p>You are " + age + " years old.</p>");
                out.println("<p>Status: <strong>ADULT</strong></p>");
                out.println("<p><a href='/'>Back to form</a></p>");
                out.println("</body></html>");
            } else {
                // Minor - show restricted page
                out.println("<html><body>");
                out.println("<h1>Access Denied</h1>");
                out.println("<p>You are only " + age + " years old.</p>");
                out.println("<p>Status: <strong>MINOR</strong> (18+ required)</p>");
                out.println("<p><a href='/'>Back to form</a></p>");
                out.println("</body></html>");
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        } catch (NumberFormatException e) {
            sendErrorPage(out, "Invalid age format. Please enter numbers only.");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void sendErrorPage(PrintWriter out, String message) {
        out.println("<html><body>");
        out.println("<h1>Error</h1>");
        out.println("<p>" + message + "</p>");
        out.println("<p><a href='/'>Try again</a></p>");
        out.println("</body></html>");
    }
}