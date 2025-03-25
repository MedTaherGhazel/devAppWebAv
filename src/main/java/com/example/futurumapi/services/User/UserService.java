package com.example.futurumapi.services.User;

import com.example.futurumapi.entities.Role;
import com.example.futurumapi.entities.User;

import java.util.List;

public interface UserService {
    void assignArticleToUser(Long userId, Long articleId);
    User getUserById(Long userId);
    List<User> getAllUsers();
    User createMedrator(User user);
    void deleteUser(Long id);
    User editUser(Long id, User user);
    List<User> getUsersByRole(Role role);
}
