package com.ttrip.core.repository.chatMessage;

import com.ttrip.core.entity.chatmessage.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findByChatroomId(Integer chatRoomId);

    List<ChatMessage> findAll();
}
//import com.ttrip.core.entity.chatroom.ChatMessage;
//import org.springframework.data.mongodb.repository.MongoRepository;
//
//import java.util.List;
//
//public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
//
//    List<ChatMessage> findByRoomId(String roomId);
//    ChatMessage save(ChatMessage chatMessage);
//    void deleteByRoomId(String roomId);
//}