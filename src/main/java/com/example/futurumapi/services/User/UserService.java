package com.example.futurumapi.services.User;
import com.example.futurumapi.entities.User;

import java.util.List;

public interface UserService {
    void assignArticleToUser(Long userId, Long articleId);
    User getUserById(Long userId);
    List<User> getAllUsers();
    void deleteUser(Long userId);
    void updateUserRole(Long userId, String role);
    long countUsers();
}
