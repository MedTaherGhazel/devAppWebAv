package com.example.futurumapi.services.User;

import com.example.futurumapi.entities.Article;
import com.example.futurumapi.entities.Role;
import com.example.futurumapi.entities.User;
import com.example.futurumapi.repositories.ArticleRepository;
import com.example.futurumapi.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           ArticleRepository articleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;

        this.passwordEncoder = passwordEncoder;
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
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();  // Fetch all users
    }

    public User createMedrator(User user) {
        try {
            // Check if user already exists
            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                System.out.println("User with email " + user.getEmail() + " already exists.");
                throw new RuntimeException("User already exists");
            }

            // Encrypt password
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Save new user
            return userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
            throw new RuntimeException("Bad Request: Unable to create user");
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);  // Delete user by ID
    }
    @Override
    public User editUser(Long id , User user){
      return   userRepository.findById(id).map(existingUser ->{
            existingUser.setFname(user.getFname());
            existingUser.setLname(user.getLname());
            return userRepository.save(existingUser);
        }).orElseThrow(()->new RuntimeException("User not found"));
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }
}