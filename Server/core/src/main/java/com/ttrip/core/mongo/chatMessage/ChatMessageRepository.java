package com.ttrip.core.mongo.chatMessage;

import com.ttrip.core.entity.chatmessage.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, Integer> {
    List<ChatMessage> findByChatroomId(Integer chatRoomId);
}