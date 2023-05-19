package com.ttrip.core.entity.chatMember;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ttrip.core.entity.BaseEntity;
import com.ttrip.core.entity.chatroom.Chatroom;
import com.ttrip.core.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer chatMemberId;
    @ManyToOne
    @JoinColumn(name = "ParticipantId")
    @JsonManagedReference
    private Member member;
    @ManyToOne
    @JoinColumn(name = "chatRoomId")
    @JsonManagedReference
    private Chatroom chatroom;
}
