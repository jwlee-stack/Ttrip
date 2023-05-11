package com.ttrip.core.entity.badge;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ttrip.core.entity.BaseEntity;
import com.ttrip.core.entity.chatroom.Chatroom;
import com.ttrip.core.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
