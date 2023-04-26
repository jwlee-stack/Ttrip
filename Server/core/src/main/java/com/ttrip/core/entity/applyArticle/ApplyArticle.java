package com.ttrip.core.entity.applyArticle;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ttrip.core.entity.BaseEntity;
import com.ttrip.core.entity.article.Article;
import com.ttrip.core.entity.member.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyArticle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String requestContent;
    private char status;
    @ManyToOne
    @JoinColumn(name = "applicatntId")
    @JsonManagedReference
    private Member member;
    @ManyToOne
    @JoinColumn(name = "articleId")
    @JsonManagedReference
    private Article article;

}
