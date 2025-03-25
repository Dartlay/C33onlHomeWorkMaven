package org.time.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter({"/time/*", "/api/*"})
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

        // Обработка отправки формы
        if ("POST".equalsIgnoreCase(req.getMethod()) && req.getParameter("age") != null) {
            handleAgeVerification(req, resp);
            return;
        }

        // Показ формы для всех остальных случаев
        showAgeForm(req, resp);
    }

    private void handleAgeVerification(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int age = Integer.parseInt(req.getParameter("age"));
            String target = req.getParameter("target") != null ? req.getParameter("target") : "/time/minsk";

            if (age >= 18) {
                req.getSession().setAttribute("ageVerified", true);
                resp.sendRedirect(target);
            } else {
                showErrorPage(resp, "Access denied! You must be at least 18 years old.");
            }
        } catch (NumberFormatException e) {
            showErrorPage(resp, "Invalid age format. Please enter a number between 1 and 120.");
        }
    }

    private void showAgeForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <title>Age Verification</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; background: #f5f5f5; margin: 0;");
        out.println("               display: flex; justify-content: center; align-items: center;");
        out.println("               min-height: 100vh; }");
        out.println("        .container { background: white; padding: 2rem; border-radius: 8px;");
        out.println("                   box-shadow: 0 2px 10px rgba(0,0,0,0.1); width: 100%;");
        out.println("                   max-width: 400px; text-align: center; }");
        out.println("        h1 { color: #2c3e50; margin-bottom: 1.5rem; }");
        out.println("        input { width: 100%; padding: 0.75rem; margin: 0.5rem 0;");
        out.println("               border: 1px solid #ddd; border-radius: 4px; font-size: 1rem; }");
        out.println("        button { background: #4CAF50; color: white; border: none;");
        out.println("                padding: 0.75rem; width: 100%; border-radius: 4px;");
        out.println("                font-size: 1rem; cursor: pointer; margin-top: 0.5rem; }");
        out.println("        .error { color: #e74c3c; margin-top: 1rem; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class=\"container\">");
        out.println("        <h1>Age Verification</h1>");
        out.println("        <form method=\"POST\">");
        out.println("            <input type=\"hidden\" name=\"target\" value=\"" + req.getRequestURI() + "\">");
        out.println("            <input type=\"number\" name=\"age\" min=\"1\" max=\"120\"");
        out.println("                   placeholder=\"Enter your age (1-120)\" required>");
        out.println("            <button type=\"submit\">Verify Age</button>");

        if (req.getParameter("error") != null) {
            out.println("            <div class=\"error\">" + req.getParameter("error") + "</div>");
        }

        out.println("        </form>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }

    private void showErrorPage(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <title>Access Denied</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; text-align: center;");
        out.println("               padding: 2rem; }");
        out.println("        h1 { color: #e74c3c; }");
        out.println("        p { margin: 1rem 0; }");
        out.println("        a { color: #3498db; text-decoration: none; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <h1>Access Denied</h1>");
        out.println("    <p>" + message + "</p>");
        out.println("    <p><a href=\"javascript:history.back()\">Try again</a></p>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}