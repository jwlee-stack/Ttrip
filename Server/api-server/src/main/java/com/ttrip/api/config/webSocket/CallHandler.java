package com.ttrip.api.config.webSocket;

import com.google.gson.Gson;
import com.ttrip.api.dto.Call.CallPayloadDto;
import com.ttrip.core.repository.call.CallRedisDao;
import com.ttrip.core.repository.liveRedisDao.LiveRedisDao;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.repository.surveyRedisDao.SurveyRedisDao;
import com.ttrip.core.utils.EuclideanDistanceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class CallHandler extends TextWebSocketHandler {
    // 동시성 제어를 위한 동기화 블록(synchronized block)이나 별도의 락(lock) 없이도 안전한 동작을 보장합니다.
    private final static ConcurrentHashMap<String, WebSocketSession> clients =
            new ConcurrentHashMap<>();
    private final Logger logger = LogManager.getLogger(CallHandler.class);
    private final CallRedisDao callRedisDao;
    public CallHandler(CallRedisDao callRedisDao) {
        this.callRedisDao = callRedisDao;
    }

    // connection established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, String> vars = getPathVariable(session);
        logger.info(String.format("********** CallService : %s is connected **********", vars.get("memberUuid")));
        clients.put(vars.get("memberUuid"), session);
    }

    /**
     * 통화를 하기 위한 상태 정보를 제공합니다.
     *
     * @param session : 통화할 유저의 session
     * @param message : type, sessionId, memberUuid, otherUuid 입력된 string message
     * @throws Exception : Unexpected Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 유저의 상태 정보
        CallPayloadDto payload = new Gson().fromJson(message.getPayload(), CallPayloadDto.class);
        // 발신
        if(payload.getType().equals("call")) {
            //상대한테 수신 알리기
            sendMessageToCall(payload);
            logger.info(String.format("********** CallService : %s call %s : %s **********", payload.getMemberUuid(), payload.getOtherUuid(), payload.getSessionId()));
        } else if(payload.getType().equals("ok")){
            callRedisDao.saveCallSession(payload.getSessionId(), payload.getMemberUuid());
            callRedisDao.saveCallSession(payload.getSessionId(), payload.getOtherUuid());
            sendMessageToResult(payload, "success");
        } else if(payload.getType().equals("no")) {
            sendMessageToResult(payload, "fail");
        } else if(payload.getType().equals("end")){
            callRedisDao.deleteMember(payload.getSessionId(), payload.getMemberUuid());
            callRedisDao.deleteMember(payload.getSessionId(), payload.getOtherUuid());
        }

    }

    // connection closed

    /**
     * 종료된 세션은 메모리 상에서 제거합니다. 종료된 세션에 해당하는 유저의 정보를 상대방 유저에게 알립니다.
     *
     * @param session : 종료된 세션
     * @param status  : status
     * @throws Exception : unexpected Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Map<String, String> vars = getPathVariable(session);
        clients.remove(vars.get("memberUuid"));
        logger.info(String.format("********** CallService : %s is disconnected **********", vars.get("memberUuid")));

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        Map<String, String> vars = getPathVariable(session);
        //
        logger.info(String.format("********** CallService : %s is disconnected by unexpected session failure. **********", vars.get("memberUuid")));
        super.handleTransportError(session, exception);
    }


    private void sendMessageToCall(CallPayloadDto payload) throws Exception {
        // otherUuid가 이미 다른 세션에 들어가 있는지 확인하는 코드 추가 예정
        String member = payload.getOtherUuid().toString(); //발신자
        String other = payload.getMemberUuid().toString(); //수신자
        payload.setType("receive");
        // 상대가 이미 통화중이라면 유저에게 이미 통화중이라고 알림
        if (callRedisDao.findSessionIdbyMemberUuid(payload.getOtherUuid())) {
            member = payload.getMemberUuid();
            other = payload.getOtherUuid();
            payload.setType("already");
        }
        // 그렇지 않다면 상대에게 알림
        WebSocketSession callSession = clients.get(other);
        logger.info("callservice otherUuid : " + other);
        if (!Objects.isNull(callSession) && callSession.isOpen()) {
            logger.info("callservice 수신 연결되어있음");
            callSession.sendMessage(
                    new TextMessage(
                            new Gson().toJson(
                                    CallPayloadDto.builder()
                                            .type(payload.getType())
                                            .sessionId(payload.getSessionId())
                                            .memberUuid(other)
                                            .otherUuid(member)
                                            .build()))
            );
        }
    }

    private void sendMessageToResult(CallPayloadDto payload, String result) throws Exception {
        List<String> members = new ArrayList<>();
        members.add(payload.getMemberUuid());
        members.add(payload.getOtherUuid());
        for (String member : members) {
            String other = (payload.getMemberUuid().equals(member)) ? payload.getOtherUuid() : payload.getMemberUuid();
            WebSocketSession callSession = clients.get(member);
            if (!Objects.isNull(callSession) && callSession.isOpen()) {
                callSession.sendMessage(
                        new TextMessage(
                                new Gson().toJson(
                                        CallPayloadDto.builder()
                                                .type(result)
                                                .sessionId(payload.getSessionId())
                                                .memberUuid(member)
                                                .otherUuid(other)
                                                .build()))
                );
            }
        }
    }

    private Map<String, String> getPathVariable(WebSocketSession session) {
        Map<String, String> vars = new HashMap<>();
        vars.put("otherUuid", (String) session.getAttributes().get("otherUuid"));
        vars.put("memberUuid", (String) session.getAttributes().get("memberUuid"));
        return vars;
    }
}