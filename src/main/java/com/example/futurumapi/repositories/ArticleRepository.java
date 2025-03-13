package com.example.futurumapi.repositories;

import com.example.futurumapi.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
        @Query("SELECT DISTINCT a FROM Article a " +
                "LEFT JOIN a.tags t " +
                "WHERE " +
                "LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(a.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(t) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<Article> searchByKeyword(@Param("keyword") String keyword);

}
