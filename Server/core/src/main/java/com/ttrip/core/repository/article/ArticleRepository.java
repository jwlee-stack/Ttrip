package com.ttrip.core.repository.article;

import com.ttrip.core.entity.article.Article;
import com.ttrip.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository <Article, Integer> {
    List<Article> findAllByOrderByEndDateAsc();
    List<Article> findByNationOrderByEndDateAsc(String nation);
    List<Article> findByCityOrderByEndDateAsc(String city);
    List<Article> findByTitleOrContentContainingOrderByEndDateAsc(String keyword, String keyword2);
    Optional<Article> findByArticleId(Integer id);
    List<Article> findAllByMember(Member member);
}
