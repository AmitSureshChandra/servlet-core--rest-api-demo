package org.example.controller;

import org.example.annotation.*;
import org.example.model.User;
import org.example.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController("/users")
public class UserRestController {
    private final UserService userService = new UserService();

    @Get
    public List<User> getAllUsers(HttpServletRequest req, HttpServletResponse resp) {
        return userService.getAllUsers();
    }

    @Get("/{id}")
    public User getUserById(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        String[] parts = pathInfo.split("/");
        if (parts.length > 2) {
            Long id = Long.parseLong(parts[2]);
            User user = userService.getUserById(id);
            if (user == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            return user;
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    @Post
    public User createUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = new com.fasterxml.jackson.databind.ObjectMapper().readValue(req.getReader(), User.class);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        return userService.createUser(user);
    }

    @Put("/{id}")
    public User updateUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String pathInfo = req.getPathInfo();
        String[] parts = pathInfo.split("/");
        if (parts.length > 2) {
            Long id = Long.parseLong(parts[2]);
            User user = new com.fasterxml.jackson.databind.ObjectMapper().readValue(req.getReader(), User.class);
            user.setId(id);
            return userService.createUser(user); // Simple update implementation
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    @Delete("/{id}")
    public void deleteUser(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}