package com.ttrip.core.repository.chatMessage;

import com.ttrip.core.entity.chatmessage.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository <ChatMessage, Integer> {
    List<ChatMessage> findByChatroomId(Integer chatRoomId);
}
