package com.ttrip.core.entity.chatroom;

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
public class Chatroom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer chatRoomId;
    private char status;
    @ManyToOne
    @JoinColumn(name = "memberId")
    @JsonManagedReference
    private Member member;
    @ManyToOne
    @JoinColumn(name = "articleId")
    @JsonManagedReference
    private Article article;

}