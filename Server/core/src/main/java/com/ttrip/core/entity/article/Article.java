package com.ttrip.core.entity.article;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ttrip.core.entity.applyArticle.ApplyArticle;
import com.ttrip.core.entity.chatroom.Chatroom;
import com.ttrip.core.entity.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private LocalDateTime createdDate;
    private String content;
    private String nation;
    private String city;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private char status;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonManagedReference
    private Member member;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ApplyArticle> applyArticleList;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Chatroom> chatroomList;

    @Builder
    public Article(String title, String content, String nation, String city, LocalDateTime startDate, LocalDateTime endDate, Member member) {
        this.title = title;
        this.createdDate = LocalDateTime.now();
        this.content = content;
        this.nation = nation;
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = 'T';
        this.member = member;
    }
}
