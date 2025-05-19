package org.example.web;

import org.example.dto.UserDTO;
import org.example.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class UserServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setPassword("secure123");
        user.setEmail("john@example.com");
        user.setAddress("123 Main St");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());


        request.setAttribute("user", userDTO);
        request.getRequestDispatcher("/user-info.jsp").forward(request, response);
    }
}