package com.ttrip.core.entity.chatmessage;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private String chatMessageId;
    private Integer chatroomId;
    //    @Type(type="org.hibernate.type.UUIDCharType")
    //hibernate는 관계형인거 같아서 잘모르겠어요
    @Column(columnDefinition = "char(36)")
    private String memberUuid;
    private LocalDateTime createdAt;
    private String text;
}