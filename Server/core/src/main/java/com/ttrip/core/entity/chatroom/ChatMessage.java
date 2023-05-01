package com.ttrip.core.entity.chatroom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Document(collection = "chat_messages")
public class ChatMessage {
    @Id
    private String id;
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime timestamp;

    @Builder
    public ChatMessage(String roomId, String sender, String message, LocalDateTime timestamp) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }
}
