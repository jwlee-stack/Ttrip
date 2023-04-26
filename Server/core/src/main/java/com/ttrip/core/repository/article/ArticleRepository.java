package com.ttrip.core.repository.article;

import com.ttrip.core.entity.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository <Article, Integer> {
    List<Article> findAll();
    List<Article> findByNation(String nation);
    List<Article> findByCity(String city);
    List<Article> findByTitleOrContentContaining(String keyword, String keyword2);
    Optional<Article> findByArticleId(Integer id);
}
