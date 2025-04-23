package org.example.controller;

import org.example.dto.UserDto;
import org.example.exception.UserNotFoundException;
import org.example.exception.ValidationException;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.JsonConverter;
import org.example.util.RequestParser;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;



public class UserController {
    private final UserService userService;
    private final JsonConverter jsonConverter;
    private final RequestParser requestParser;

    public UserController(UserService userService, JsonConverter jsonConverter, RequestParser requestParser) {
        this.userService = userService;
        this.jsonConverter = jsonConverter;
        this.requestParser = requestParser;
    }
    public void getUserById(String idStr, HttpServletResponse response) throws IOException {
        try {
            Long id = parseId(idStr);
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                sendSuccessResponse(response, jsonConverter.toJson(user.get()));
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } catch (ValidationException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public void createUser(String requestBody, HttpServletResponse response) throws IOException {
        try {
            UserDto userDto = jsonConverter.fromJson(requestBody, UserDto.class);
            User user = new User(null, userDto.getLogin(), userDto.getName(), userDto.getEmail());
            User createdUser = userService.createUser(user);
            sendSuccessResponse(response, jsonConverter.toJson(createdUser));
        } catch (ValidationException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public void updateUserLogin(String idStr, String newLogin, HttpServletResponse response)
            throws IOException {
        try {
            Long id = parseId(idStr);
            userService.updateUserLogin(id, newLogin);
            sendSuccessResponse(response, "Login updated successfully");
        } catch (ValidationException | UserNotFoundException e) {
            int status = e instanceof UserNotFoundException
                    ? HttpServletResponse.SC_NOT_FOUND
                    : HttpServletResponse.SC_BAD_REQUEST;
            sendErrorResponse(response, status, e.getMessage());
        }
    }

    public void deleteUser(String idStr, HttpServletResponse response) throws IOException {
        try {
            Long id = parseId(idStr);
            userService.deleteUser(id);
            sendSuccessResponse(response, "User deleted successfully");
        } catch (ValidationException | UserNotFoundException e) {
            int status = e instanceof UserNotFoundException
                    ? HttpServletResponse.SC_NOT_FOUND
                    : HttpServletResponse.SC_BAD_REQUEST;
            sendErrorResponse(response, status, e.getMessage());
        }
    }

    private Long parseId(String idStr) throws ValidationException {
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid ID format");
        }
    }

    private void sendSuccessResponse(HttpServletResponse response, String message)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(message);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}