package com.example.futurumapi.services.User;
import com.example.futurumapi.entities.Role;
import com.example.futurumapi.entities.User;
import com.example.futurumapi.repositories.UserRepository;
import com.example.futurumapi.repositories.ArticleRepository;
import com.example.futurumapi.entities.Article;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public UserServiceImpl(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void assignArticleToUser(Long userId, Long articleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        user.getContributedArticles().add(article);
        article.getContributors().add(user);

        userRepository.save(user);
        articleRepository.save(article);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public void updateUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(Role.valueOf(role)); // Assuming there's a setRole method
        userRepository.save(user);
    }
    @Override
    public long countUsers() {
        return userRepository.count();
    }

}
