package com.example.futurumapi.test;

import com.example.futurumapi.entities.Article;
import com.example.futurumapi.entities.Domain;
import com.example.futurumapi.entities.Role;
import com.example.futurumapi.entities.User;
import com.example.futurumapi.repositories.ArticleRepository;
import com.example.futurumapi.repositories.DomainRepository;
import com.example.futurumapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final DomainRepository domainRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public DataInitializer(UserRepository userRepository,
                           ArticleRepository articleRepository,
                           DomainRepository domainRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.domainRepository = domainRepository;
    }

    @Override
    public void run(String... args) {
        // Clear existing data
        articleRepository.deleteAll();
        domainRepository.deleteAll();
        userRepository.deleteAll();

        // Create test domains
        Domain nlpDomain = new Domain();
        nlpDomain.setName("NLP");
        Domain cybersecurityDomain = new Domain();
        cybersecurityDomain.setName("Cybersecurity");
        Domain imageDomain = new Domain();
        imageDomain.setName("Image Processing");
        domainRepository.saveAll(Arrays.asList(nlpDomain, cybersecurityDomain, imageDomain));

        // Create test users
        User adminUser = new User();
        adminUser.setFname("Admin");
        adminUser.setLname("User");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("Password@123"));
        adminUser.setUsername("admin");
        adminUser.setRole(Role.ADMIN);

        User moderatorUser = new User();
        moderatorUser.setFname("Moderator");
        moderatorUser.setLname("User");
        moderatorUser.setEmail("moderator@example.com");
        moderatorUser.setPassword(passwordEncoder.encode("Password@123"));
        moderatorUser.setUsername("moderator");
        moderatorUser.setRole(Role.MODERATOR);

        User regularUser = new User();
        regularUser.setFname("Regular");
        regularUser.setLname("User");
        regularUser.setEmail("user@example.com");
        regularUser.setPassword(passwordEncoder.encode("Password@123"));
        regularUser.setUsername("user");
        regularUser.setRole(Role.USER);

        userRepository.saveAll(Arrays.asList(adminUser, moderatorUser, regularUser));

        // Create test articles
        Article article1 = new Article();
        article1.setTitle("Introduction to NLP");
        article1.setContent("Natural Language Processing is a field of AI...");
        article1.setPubDate(new Date());
        article1.setPdf("nlp_article.pdf");
        article1.setTags(Arrays.asList("NLP", "AI"));
        article1.setDomain(nlpDomain);

        Article article2 = new Article();
        article2.setTitle("Cybersecurity Basics");
        article2.setContent("Cybersecurity is the practice of protecting systems...");
        article2.setPubDate(new Date());
        article2.setPdf("cybersecurity_article.pdf");
        article2.setTags(Arrays.asList("Cybersecurity", "Networking"));
        article2.setDomain(cybersecurityDomain);

        articleRepository.saveAll(Arrays.asList(article1, article2));

        // Assign articles to users (many-to-many relationship)
        adminUser.getContributedArticles().add(article1); // Admin contributes to article1
        article1.getContributors().add(adminUser);

        moderatorUser.getContributedArticles().add(article2); // Moderator contributes to article2
        article2.getContributors().add(moderatorUser);

        // Save updated entities
        userRepository.saveAll(Arrays.asList(adminUser, moderatorUser));
        articleRepository.saveAll(Arrays.asList(article1, article2));
    }
}