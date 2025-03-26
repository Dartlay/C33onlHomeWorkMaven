package com.example.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@WebFilter("/*")
public class RequestLoggingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("[" + new Date() + "] Инициализирован фильтр логирования");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestInfo = String.format("%s %s%s",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                httpRequest.getQueryString() != null ? "?" + httpRequest.getQueryString() : "");

        System.out.println("[" + new Date() + "] Начало обработки запроса: " + requestInfo);

        long startTime = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("[" + new Date() + "] Завершение запроса (" + duration + " мс): " + requestInfo);
        }
    }

    @Override
    public void destroy() {
        System.out.println("[" + new Date() + "] Фильтр логирования уничтожен");
    }
}