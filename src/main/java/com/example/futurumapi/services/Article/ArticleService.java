package com.example.futurumapi.services.Article;

import com.example.futurumapi.dto.ArticleDTO;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    List<ArticleDTO> getAllArticles();
    Optional<ArticleDTO> getArticleById(Long id);
    ArticleDTO createArticle(ArticleDTO articleDTO);
    Optional<ArticleDTO> updateArticle(Long id, ArticleDTO articleDTO);
    List<ArticleDTO> searchArticlesByKeyword(String keyword);
    void assignArticleToDomain(Long articleId, List<String> domainNames);
    boolean deleteArticle(Long id);
}
