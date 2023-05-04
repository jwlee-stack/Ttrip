package com.ttrip.core.entity.matchHistory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ttrip.core.entity.BaseEntity;
import com.ttrip.core.entity.article.Article;
import com.ttrip.core.entity.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchHistory extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer matchHistoryId;
    private Integer rate;
    @ManyToOne
    @JsonManagedReference
    private Member evaluator;
    @ManyToOne
    @JsonManagedReference
    private Member evaluated;
    @OneToOne
    @JsonManagedReference
    private Article article;
    //예행 마감 예정일 다음날에 매칭 평가 fcm발송하기 위해서 추가
}
