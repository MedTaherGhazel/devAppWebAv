package com.example.futurumapi.services.User;

import com.example.futurumapi.entities.User;

public interface UserService {
    void assignArticleToUser(Long userId, Long articleId);
    User getUserById(Long userId);
}
