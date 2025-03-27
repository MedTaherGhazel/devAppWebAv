package com.example.futurumapi.web;

import com.example.futurumapi.dto.ArticleDTO;
import com.example.futurumapi.entities.User;
import com.example.futurumapi.services.Article.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArticleDTO>> searchArticles(@RequestParam String keyword) {
        return ResponseEntity.ok(articleService.searchArticlesByKeyword(keyword));
    }

    @PutMapping("/{articleId}/assign-domains")
    public ResponseEntity<?> assignDomainsToArticle(@PathVariable Long articleId,
                                                    @RequestBody List<String> domainNames) {
        try {
            articleService.assignArticleToDomain(articleId, domainNames);
            return ResponseEntity.ok("Domains assigned successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleDTO) {
        ArticleDTO createdArticle = articleService.createArticle(articleDTO);
        return ResponseEntity.ok(createdArticle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        Optional<ArticleDTO> updatedArticle = articleService.updateArticle(id, articleDTO);
        return updatedArticle.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        if (articleService.deleteArticle(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    //assign article to user
    @PostMapping("/{articleId}/assign-moderator/{userId}")
    public ResponseEntity<?> assignModerator(
            @PathVariable Long articleId,
            @PathVariable Long userId) {
        try {
            articleService.assignModeratorToArticle(articleId, userId);
            return ResponseEntity.ok("Moderator assigned successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{articleId}/moderators")
    public ResponseEntity<Set<User>> getArticleModerators(@PathVariable Long articleId) {
        return ResponseEntity.ok(articleService.getArticleModerators(articleId));
    }

    @DeleteMapping("/{articleId}/remove-moderator/{userId}")
    public ResponseEntity<?> removeModerator(
            @PathVariable Long articleId,
            @PathVariable Long userId) {
        try {
            articleService.removeModeratorFromArticle(articleId, userId);
            return ResponseEntity.ok("Moderator removed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
