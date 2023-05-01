package com.ttrip.core.chat;

import com.ttrip.core.entity.chatroom.ChatMessage;
import com.ttrip.core.repository.chatroom.ChatMessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ChatMessageTest {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private final String roomId = "testRoom";

    @AfterEach
    public void initialize() {
        chatMessageRepository.deleteByRoomId(roomId);
    }

    @Test
    @DisplayName("몽고디비 연동 테스트")
    void chatMessageTest() {
        String sender = "testSender";
        ChatMessage m = ChatMessage.builder()
                .roomId(roomId)
                .timestamp(LocalDateTime.now())
                .sender(sender)
                .message("몽고디비 연결 성공")
                .build();
        ChatMessage savedM = chatMessageRepository.save(m);
        assertEquals("몽고디비 연결 성공", savedM.getMessage());

        ChatMessage queryedM = chatMessageRepository.findById(savedM.getId()).orElse(null);
        assertEquals("몽고디비 연결 성공", queryedM.getMessage());
    }
}
