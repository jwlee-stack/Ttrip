package com.ttrip.api.config.webSocket;

import com.google.gson.Gson;
import com.ttrip.api.dto.chatroomDto.ChatSocketResDto;
import com.ttrip.api.dto.fcmMessageDto.FcmMessageReqDto;
import com.ttrip.api.service.FcmService;
import com.ttrip.api.util.BadWordFilterUtil;
import com.ttrip.core.entity.chatmessage.ChatMessage;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.mongo.chatMessage.ChatMessageRepository;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
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
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler {
    private final MemberRepository memberRepository;
    private final FcmService fcmService;
    // session 저장 맵
    private final HashMap<String, WebSocketSession> sessionMap = new HashMap<>();
    private final ChatMessageRepository chatMessageRepository;
    private final BadWordFilterUtil badWordFilterUtil;
    private final Logger logger = LogManager.getLogger(ChatHandler.class);

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String memberUuid = session.getAttributes().get("memberUuid").toString();
        String opponentUuid = session.getAttributes().get("opponentUuid").toString();
        Integer chatroomId = Integer.parseInt(session.getAttributes().get("chatroomId").toString());
        ChatSocketResDto chatSocketResDto = ChatSocketResDto.builder()
                .isMine(true)
                .content(badWordFilterUtil.filter(message.getPayload()))
                .createdAt(LocalDateTime.now().toString())
                .build();

        Gson gson = new Gson();
        //json으로 파싱
        TextMessage textMessage = new TextMessage(gson.toJson(chatSocketResDto));
        //나한테 보내기
        sessionMap.get(memberUuid).sendMessage(textMessage);

        //상대 접속중이면 보내기
        if (sessionMap.get(opponentUuid) != null) {
            chatSocketResDto.setIsMine(false);
            sessionMap.get(opponentUuid).sendMessage(new TextMessage(gson.toJson(chatSocketResDto)));
        } else{
            Member member = memberRepository.findByMemberUuid(UUID.fromString(memberUuid))
                    .orElseThrow(()->new NoSuchElementException(ErrorMessageEnum.USER_NOT_EXIST.getMessage()));
            //상대가 채팅방에 없으나 fcm토큰이 있으면 fcm 메세지 전송
            fcmService.sendMessageTo(member, FcmMessageReqDto.builder()
                    .type(3)
                    .targetUuid(UUID.fromString(opponentUuid))
                    .extraId(chatroomId.toString())
                    .extraData(message.getPayload())
                    .build()
            );
        }
        //db에 저장
        chatMessageRepository.save(ChatMessage.builder()
                .text(chatSocketResDto.getContent())
                .memberUuid(memberUuid)
                .createdAt(LocalDateTime.parse(chatSocketResDto.getCreatedAt()))
                .chatroomId(chatroomId)
                .build());
        logger.info("chatting: " + memberUuid + " send Message to " + opponentUuid);
    }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String memberUuid = session.getAttributes().get("memberUuid").toString();
        sessionMap.put(memberUuid, session);
        logger.info("chatting: " + memberUuid + " access");
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionMap.remove(session.getAttributes().get("memberUuid"));
        logger.info("chatting: " + session.getAttributes().get("memberUuid") + " exit");
    }
}