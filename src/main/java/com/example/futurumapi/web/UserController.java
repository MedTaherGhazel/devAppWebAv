package com.example.futurumapi.web;

import com.example.futurumapi.services.User.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{userId}/assign-article/{articleId}")
    public ResponseEntity<?> assignArticleToUser(@PathVariable Long userId,
                                                 @PathVariable Long articleId) {
        try {
            userService.assignArticleToUser(userId, articleId);
            return ResponseEntity.ok("Article assigned successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}