package com.example.futurumapi.dao;

import com.example.futurumapi.dto.ArticleDTO;
import com.example.futurumapi.entities.Article;
import com.example.futurumapi.repositories.ArticleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ArticleDAO {

    private final ArticleRepository articleRepository;

    public ArticleDAO(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    public List<ArticleDTO> findAllWithDetails() {
        return articleRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<ArticleDTO> findByIdWithDetails(Long id) {
        return articleRepository.findById(id).map(this::convertToDTO);
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
        dto.setContributorIds(article.getContributors().stream().map(user -> user.getId()).collect(Collectors.toSet()));
        return dto;
    }
}
