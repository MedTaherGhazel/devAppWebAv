package com.example.futurumapi.services.User;

import com.example.futurumapi.entities.Article;
import com.example.futurumapi.entities.User;
import com.example.futurumapi.repositories.ArticleRepository;
import com.example.futurumapi.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void assignArticleToUser(Long userId, Long articleId) {
        // Fetch the user and article from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        // Add the article to the user's contributedArticles set
        user.getContributedArticles().add(article);

        // Add the user to the article's contributors set
        article.getContributors().add(user);

        // Save both entities (optional due to cascading)
        userRepository.save(user);
        articleRepository.save(article);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}