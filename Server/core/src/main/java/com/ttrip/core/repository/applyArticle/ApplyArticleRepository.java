package com.ttrip.core.repository.applyArticle;

import com.ttrip.core.entity.applyArticle.ApplyArticle;
import com.ttrip.core.entity.article.Article;
import com.ttrip.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplyArticleRepository extends JpaRepository <ApplyArticle, Integer> {
    List<ApplyArticle> findByArticle(Article article);
    Optional<ApplyArticle> findByArticleAndMember(Article article, Member member);
    Boolean existsByArticleAndMember(Article article, Member member);
}
