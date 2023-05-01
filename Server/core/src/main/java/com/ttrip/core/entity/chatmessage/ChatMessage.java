package com.ttrip.core.entity.chatmessage;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

//mongodb로 바꿀꺼라 관계형 없어요
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
    private Integer chatMessageId;
    private Integer chatroomId;
    //    @Type(type="org.hibernate.type.UUIDCharType")
    //hibernate는 관계형인거 같아서 잘모르겠어요
    @Column(columnDefinition = "char(36)")
    private String memberUuid;
    private LocalDateTime createdAt;
    private String text;
}

//import lombok.Builder;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import javax.persistence.Id;
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@ToString
//@Document(collection = "chat_messages")
//public class ChatMessage {
//    @Id
//    private String id;
//    private String roomId;
//    private String sender;
//    private String message;
//    private LocalDateTime timestamp;
//
//    @Builder
//    public ChatMessage(String roomId, String sender, String message, LocalDateTime timestamp) {
//        this.roomId = roomId;
//        this.sender = sender;
//        this.message = message;
//        this.timestamp = timestamp;
//    }
//}