package com.ttrip.core.entity.chatroom;

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
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private char status;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "memberId")
    @JsonManagedReference
    private Member member;

    @ManyToOne
    @JoinColumn(name = "articleId")
    @JsonManagedReference
    private Article article;

    @Builder
    public Chatroom(Member member, Article article){
        this.status = 'T';
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        this.member = member;
        this.article = article;
    }
}