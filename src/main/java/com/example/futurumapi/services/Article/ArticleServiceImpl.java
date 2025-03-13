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
        dto.setDomainName(article.getDomain().getName()); // Include domain name
        return dto;
    }
}
