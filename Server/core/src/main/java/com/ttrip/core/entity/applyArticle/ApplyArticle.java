package com.ttrip.core.entity.applyArticle;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ttrip.core.entity.article.Article;
import com.ttrip.core.entity.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class ApplyArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String requestContent;

    private char status;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "applicatntId")
    @JsonManagedReference
    private Member member;

    @ManyToOne
    @JoinColumn(name = "articleId")
    @JsonManagedReference
    private Article article;

    @Builder
    public ApplyArticle(String requestContent, Member member, Article article){
        this.requestContent = requestContent;
        this.status = 'T';
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        this.member = member;
        this.article = article;
    }
}
