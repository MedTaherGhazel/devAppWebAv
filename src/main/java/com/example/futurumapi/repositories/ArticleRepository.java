package com.example.futurumapi.repositories;

import com.example.futurumapi.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
        @Query("SELECT DISTINCT a FROM Article a " +
                "LEFT JOIN a.tags t " +
                "WHERE " +
                "LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(a.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(t) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<Article> searchByKeyword(@Param("keyword") String keyword);
        @Query("SELECT a FROM Article a LEFT JOIN FETCH a.contributors WHERE a.id = :id")
        Optional<Article> findByIdWithContributors(@Param("id") Long id);
}
