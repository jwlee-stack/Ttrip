package com.ttrip.core.repository.chatroom;

import com.ttrip.core.entity.chatroom.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByRoomId(String roomId);
    ChatMessage save(ChatMessage chatMessage);
    void deleteByRoomId(String roomId);
}
