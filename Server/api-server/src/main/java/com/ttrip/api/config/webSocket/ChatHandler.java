package com.ttrip.api.config.webSocket;

import com.google.gson.Gson;
import com.ttrip.api.dto.chatroomDto.ChatMessageResDto;
import com.ttrip.core.entity.chatmessage.ChatMessage;
import com.ttrip.core.repository.chatMessage.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.HashMap;

@RequiredArgsConstructor
@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler {
    // session 저장 맵
    private final HashMap<String, WebSocketSession> sessionMap = new HashMap<>();
    private final ChatMessageRepository chatMessageRepository;
    private final Logger logger = LogManager.getLogger(ChatHandler.class);

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String memberUuid = session.getAttributes().get("memberUuid").toString();
        String opponentUuid = session.getAttributes().get("opponentUuid").toString();
        Integer chatroomId = Integer.parseInt(session.getAttributes().get("chatroomId").toString());
        ChatMessageResDto chatMessageResDto = ChatMessageResDto.builder()
                .isMine(true)
                .content(message.getPayload())
                .createdAt(LocalDateTime.now())
                .build();

        Gson gson = new Gson();
        //json으로 파싱
        TextMessage textMessage = new TextMessage(gson.toJson(chatMessageResDto));
        //나한테 보내기
        sessionMap.get(memberUuid).sendMessage(textMessage);

        //상대 접속중이면 보내기
        if (sessionMap.get(opponentUuid) != null) {
            chatMessageResDto.setIsMine(false);
            sessionMap.get(opponentUuid).sendMessage(textMessage);
        }
        //db에 저장
        chatMessageRepository.save(ChatMessage.builder()
                .text(chatMessageResDto.getContent())
                .memberUuid(memberUuid)
                .createdAt(chatMessageResDto.getCreatedAt())
                .chatroomId(chatroomId)
                .build());
        logger.info(memberUuid + "send Message to" + opponentUuid);
    }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String memberUuid = session.getAttributes().get("memberUuid").toString();
        sessionMap.put(memberUuid, session);
        logger.info(memberUuid + "access");
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionMap.remove(session.getAttributes().get("memberUuid"));
        logger.info(session.getAttributes().get("memberUuid") + "exit");
    }
}