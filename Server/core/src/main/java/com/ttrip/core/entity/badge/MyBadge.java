package com.ttrip.core.entity.badge;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ttrip.core.entity.BaseEntity;
import com.ttrip.core.entity.member.Member;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class MyBadge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int myBadgeId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    @JsonBackReference
    private Member member;

    @ManyToOne
    @JoinColumn(name = "badgeId")
    @JsonBackReference
    private Badge badge;
}
