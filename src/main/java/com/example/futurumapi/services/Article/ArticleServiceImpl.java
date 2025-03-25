package com.example.futurumapi.services.Article;


import com.example.futurumapi.dao.ArticleDAO;
import com.example.futurumapi.dto.ArticleDTO;
import com.example.futurumapi.entities.Article;
import com.example.futurumapi.entities.Domain;
import com.example.futurumapi.entities.User;
import com.example.futurumapi.repositories.ArticleRepository;
import com.example.futurumapi.repositories.DomainRepository;
import com.example.futurumapi.repositories.UserRepository;
import com.example.futurumapi.services.User.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleDAO articleDAO;
    private final DomainRepository domainRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleDAO articleDAO, DomainRepository domainRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.articleDAO = articleDAO;
        this.domainRepository = domainRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public List<ArticleDTO> getAllArticles() {
        return articleDAO.findAllWithDetails();
    }

    @Override
    public Optional<ArticleDTO> getArticleById(Long id) {
        return articleDAO.findByIdWithDetails(id);
    }

    @Override
    public ArticleDTO createArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        mapDtoToEntity(articleDTO, article);
        Article savedArticle = articleDAO.save(article);
        return articleDAO.findByIdWithDetails(savedArticle.getId()).orElseThrow();
    }

    @Override
    public Optional<ArticleDTO> updateArticle(Long id, ArticleDTO articleDTO) {
        Optional<Article> existingArticle = articleDAO.findById(id);
        if (existingArticle.isPresent()) {
            Article article = existingArticle.get();
            mapDtoToEntity(articleDTO, article);
            articleDAO.save(article);
            return articleDAO.findByIdWithDetails(id);
        }
        return Optional.empty();
    }
    @Override
    public List<ArticleDTO> searchArticlesByKeyword(String keyword) {
        // Fetch articles from the repository
        List<Article> articles = articleRepository.searchByKeyword(keyword.toLowerCase());

        // Convert Article entities to ArticleDTO objects
        return articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void assignArticleToDomain(Long articleId, List<String> domainNames) {
        // Fetch the article by ID
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        // Fetch the domains by their names
        List<Domain> domains = domainRepository.findByNameIn(domainNames);
        if (domains.isEmpty()) {
            throw new RuntimeException("No valid domains found");
        }

        // Assign the first domain to the article
        article.setDomain(domains.getFirst());

        // Save the updated article
        articleRepository.save(article);
    }

    @Override
    public boolean deleteArticle(Long id) {
        if (articleDAO.findById(id).isPresent()) {
            articleDAO.deleteById(id);
            return true;
        }
        return false;
    }

    private void mapDtoToEntity(ArticleDTO dto, Article article) {
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setDescription(dto.getDescription());
        article.setPubDate(dto.getPubDate());
        article.setPdf(dto.getPdf());
        article.setTags(dto.getTags());

        Domain domain = domainRepository.findByName(dto.getDomainName()).orElseThrow(() -> new RuntimeException("Domain not found"));
        article.setDomain(domain);

        Set<User> contributors = new HashSet<>(userRepository.findAllById(dto.getContributorIds()));
        article.setContributors(contributors);
    }
    private ArticleDTO convertToDTO(Article article) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setDescription(article.getDescription());
        dto.setPubDate(article.getPubDate());
        dto.setPdf(article.getPdf());
        dto.setTags(article.getTags());
        dto.setDomainName(article.getDomain().getName());

        // Add contributor info
        dto.setContributorIds(article.getContributors().stream()
                .map(User::getId)
                .collect(Collectors.toSet()));

        dto.setContributorNames(article.getContributors().stream()
                .map(u -> u.getFname() + " " + u.getLname())
                .collect(Collectors.toList()));

        return dto;
    }
    @Override
    public void assignModeratorToArticle(Long articleId, Long moderatorId) {
        // Find the article with contributors loaded
        Article article = articleRepository.findByIdWithContributors(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + articleId));

        // Find the user with role loaded
        User moderator = userRepository.findByIdWithRole(moderatorId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + moderatorId));

        // Verify the user is a moderator
        if (!"MODERATOR".equals(moderator.getRole())) {
            throw new RuntimeException("User with ID " + moderatorId + " is not a moderator");
        }

        // Check if already a contributor
        if (article.getContributors().stream()
                .anyMatch(c -> c.getId().equals(moderatorId))) {
            throw new RuntimeException("User is already a contributor to this article");
        }

        // Add to contributors and save
        article.getContributors().add(moderator);
        articleRepository.save(article);

        // If you have bidirectional relationship, also add:
        // moderator.getContributedArticles().add(article);
        // userRepository.save(moderator);
    }

}
