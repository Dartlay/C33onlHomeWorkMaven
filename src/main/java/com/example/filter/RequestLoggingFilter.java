package com.example.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

public class RequestLoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("DEBUG: RequestLoggingFilter initialized!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("DEBUG: Filter processing request");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        System.out.println(LocalDateTime.now() + " | Request: " +
                httpRequest.getMethod() + " " + httpRequest.getRequestURI());
        chain.doFilter(request, response);
    }
}