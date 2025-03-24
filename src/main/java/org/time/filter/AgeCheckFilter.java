package org.time.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter({
        "/time/*",
        "/api/*"
})
public class AgeCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (req.getSession().getAttribute("ageVerified") != null) {
            chain.doFilter(request, response);
            return;
        }
        String ageParam = req.getParameter("age");
        if (ageParam == null) {
            showAgeForm(resp, req);
            return;
        }
        try {
            int age = Integer.parseInt(ageParam);

            if (age < 1 || age > 120) {
                showError(resp, "Age must be between 1 and 120 years", req);
                return;
            }
            if (age >= 18) {
                req.getSession().setAttribute("ageVerified", true);
                chain.doFilter(request, response);
            } else {
                showError(resp, "Access denied. You must be 18+ to view this content", req);
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        } catch (NumberFormatException e) {
            showError(resp, "Invalid age format. Please enter numbers only.", req);
        }
    }

    private void showAgeForm(HttpServletResponse resp,
                             HttpServletRequest req) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Age Verification</title></head><body>");
        out.println("<h1>Age Verification Required</h1>");
        out.println("<form method='get'>");
        out.println("<p>Please enter your age (1-120):</p>");
        out.println("<input type='number' name='age' min='1' max='120' required>");
        out.println("<input type='submit' value='Verify'>");
        out.println("</form>");
        out.println("</body></html>");
    }
    private void showError(HttpServletResponse resp, String message,
                           HttpServletRequest req) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Error</title></head><body>");
        out.println("<h1>Error</h1>");
        out.println("<p>" + message + "</p>");
        out.println("<a href='" + req.getRequestURI() + "'>Try again</a>");
        out.println("</body></html>");
    }
}