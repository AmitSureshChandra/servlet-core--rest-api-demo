package com.github.AmitSureshChandra.service;

import com.github.AmitSureshChandra.model.User;
import java.util.*;

public class UserService {
    private final Map<Long, User> users = new HashMap<>();
    private Long nextId = 1L;

    public UserService() {
        users.put(1L, new User(1L, "John Doe", "john@example.com"));
        users.put(2L, new User(2L, "Jane Smith", "jane@example.com"));
        nextId = 3L;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(Long id) {
        return users.get(id);
    }

    public User createUser(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }
}