package com.ttrip.core.entity.matchHistory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ttrip.core.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchHistory {
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
}
