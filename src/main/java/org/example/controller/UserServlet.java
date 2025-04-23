package org.example.controller;

import org.example.dto.UserDto;
import org.example.exception.UserNotFoundException;
import org.example.exception.ValidationException;
import org.example.model.User;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;
import org.example.repository.UserRepository;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.util.JsonConverter;
import org.example.util.RequestParser;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {
        "/api/get",
        "/api/create",
        "/api/change-login",
        "/api/delete",
        "/users",
        "/users/new",
        "/users/view",
        "/users/edit"
})
public class UserServlet extends HttpServlet {
    private UserService userService;
    private JsonConverter jsonConverter;
    private RequestParser requestParser;

    @Override
    public void init() {
        System.out.println("Initializing UserServlet...");
        UserRepository userRepository = new UserRepositoryImpl();
        this.userService = new UserServiceImpl(userRepository);
        this.jsonConverter = new JsonConverter();
        this.requestParser = new RequestParser();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();
        System.out.println("Processing GET request for path: " + path);

        try {
            switch(path) {
                case "/api/get":
                    handleGetUser(req, resp);
                    break;
                case "/users":
                    showAllUsers(req, resp);
                    break;
                case "/users/new":
                    showCreateForm(req, resp);
                    break;
                case "/users/view":
                    showUserDetails(req, resp);
                    break;
                case "/users/edit":
                    showEditForm(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        System.out.println("Processing POST request for path: " + path);
        try {
            if ("/api/create".equals(path)) {
                if (isJsonRequest(req)) {
                    String requestBody = requestParser.parseRequestBody(req);
                    UserDto userDto = jsonConverter.fromJson(requestBody, UserDto.class);
                    User user = convertToEntity(userDto);
                    User createdUser = userService.createUser(user);
                    sendJsonResponse(resp, createdUser);
                } else {
                    String login = req.getParameter("login");
                    String name = req.getParameter("name");
                    String email = req.getParameter("email");

                    UserDto userDto = new UserDto();
                    userDto.setLogin(login);
                    userDto.setName(name);
                    userDto.setEmail(email);

                    User user = convertToEntity(userDto);
                    User createdUser = userService.createUser(user);
                    resp.sendRedirect(req.getContextPath() + "/users?created=true");
                }
            } else if ("/api/delete".equals(path)) {
                handleDeleteUser(req, resp);
            } else if ("/api/change-login".equals(path)) {
                handleUpdateLogin(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleError(resp, e);
        }
    }

    private boolean isJsonRequest(HttpServletRequest req) {
        String contentType = req.getContentType();
        return contentType != null && contentType.contains("application/json");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    }


    private void handleGetUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing ID parameter");
            return;
        }

        try {
            long id = Long.parseLong(idParam);
            Optional<User> user = userService.getUserById(id);

            if (user.isPresent()) {
                sendJsonResponse(resp, user.get());
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }
    }

    private void handleCreateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (isApiRequest(req)) {
            try {
                String requestBody = requestParser.parseRequestBody(req);
                UserDto userDto = jsonConverter.fromJson(requestBody, UserDto.class);
                User user = convertToEntity(userDto);
                User createdUser = userService.createUser(user);
                sendJsonResponse(resp, createdUser);
            } catch (Exception e) {
                handleError(resp, e);
            }
        } else {
            try {
                String login = req.getParameter("login");
                String name = req.getParameter("name");
                String email = req.getParameter("email");
                UserDto userDto = new UserDto();
                userDto.setLogin(login);
                userDto.setName(name);
                userDto.setEmail(email);
                User user = convertToEntity(userDto);
                userService.createUser(user);
                resp.sendRedirect(req.getContextPath() + "/users?created=true");
            } catch (ValidationException e) {
                req.setAttribute("error", e.getMessage());
                showCreateForm(req, resp);
            } catch (Exception e) {
                handleError(resp, e);
            }
        }
    }

    private void handleDeleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing ID parameter");
            return;
        }
        try {
            long id = Long.parseLong(idParam);
            userService.deleteUser(id);
            if (isApiRequest(req)) {
                sendSuccessMessage(resp, "User deleted successfully");
            } else {
                resp.sendRedirect(req.getContextPath() + "/users?deleted=true");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        } catch (UserNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
    private void handleUpdateLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        String newLogin = req.getParameter("login");

        if (idParam == null || newLogin == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            long id = Long.parseLong(idParam);
            userService.updateUserLogin(id, newLogin);

            if (isApiRequest(req)) {
                sendSuccessMessage(resp, "Login updated successfully");
            } else {
                resp.sendRedirect(req.getContextPath() + "/users/view?id=" + id);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }
    }


    private void showAllUsers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<User> users = userService.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/views/users/list.jsp").forward(req, resp);
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(req, resp);
    }

    private void showUserDetails(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing ID parameter");
            return;
        }

        try {
            long id = Long.parseLong(idParam);
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                req.setAttribute("user", user.get());
                req.getRequestDispatcher("/WEB-INF/views/users/view.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing ID parameter");
            return;
        }

        try {
            long id = Long.parseLong(idParam);
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                req.setAttribute("user", user.get());
                req.getRequestDispatcher("/WEB-INF/views/users/edit.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }
    }


    private boolean isApiRequest(HttpServletRequest req) {
        return req.getServletPath().startsWith("/api");
    }

    private void sendJsonResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(jsonConverter.toJson(data));
    }

    private void sendSuccessMessage(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write("{\"status\":\"success\",\"message\":\"" + message + "\"}");
    }

    private void handleError(HttpServletResponse resp, Exception e) throws IOException {
        if (e instanceof UserNotFoundException) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } else if (e instanceof ValidationException) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } else {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private User convertToEntity(UserDto dto) {
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }
}