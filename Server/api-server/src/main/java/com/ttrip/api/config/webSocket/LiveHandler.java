package com.ttrip.api.config.webSocket;

import com.google.gson.Gson;
import com.ttrip.api.dto.Live.LiveLocationResDto;
import com.ttrip.api.dto.Live.LivePayloadDto;
import com.ttrip.core.dto.live.LiveAllLocationsDto;
import com.ttrip.core.repository.liveRedisDao.LiveRedisDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LiveHandler extends TextWebSocketHandler {
    // 동시성 제어를 위한 동기화 블록(synchronized block)이나 별도의 락(lock) 없이도 안전한 동작을 보장합니다.
    private final static ConcurrentHashMap<String, WebSocketSession> clients =
            new ConcurrentHashMap<>();
    private final Logger logger = LogManager.getLogger(LiveHandler.class);
    private final LiveRedisDao liveRedisDao;
    private final static double DELETE = -1;

    public LiveHandler(LiveRedisDao liveRedisDao) {
        this.liveRedisDao = liveRedisDao;
    }

    // connection established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, String> vars = getPathVariable(session);
        logger.info(String.format("********** LiveService : %s is connected **********", vars.get("memberUuid")));
        clients.put(vars.get("memberUuid"), session);
    }

    /**
     * 본인 위치 변경 시 도시 내에 유저에게 본인 위치 정보를 제공합니다.
     *
     * @param session : 위치 변경된 유저의 session
     * @param message : memberUuid, 위도, 경도, 도시 이름이 입력된 string message
     * @throws Exception : Unexpected Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 1. 유저의 위치 변경 정보
        LivePayloadDto payload = new Gson().fromJson(message.getPayload(), LivePayloadDto.class);
        Map<String, Double> location = new HashMap<>();
        location.put("longitude", payload.getLongitude());
        location.put("latitude", payload.getLatitude());
        // 2. 유저 정보 업데이트
        liveRedisDao.saveMemberLocationInCity(payload.getCity(), payload.getMemberUuid(), location);
        // 3. 도시에 속한 유저들에게 변경 정보 전송(변경된 유저 아이디, 위치, 거리)
        sendMessageToSessionInCity(payload);
        logger.info(String.format("********** LiveService : %s sends the position **********", payload.getMemberUuid()));
    }

    // connection closed

    /**
     * 종료된 세션은 메모리 상에서 제거합니다. 종료된 세션에 해당하는 유저의 정보를 해당 도시에 속한 유저에게 알립니다.
     *
     * @param session : 종료된 세션
     * @param status  : status
     * @throws Exception : unexpected Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Map<String, String> vars = getPathVariable(session);
        liveRedisDao.deleteMemberLocationInCity(vars.get("city"), vars.get("memberUuid"));
        clients.remove(vars.get("memberUuid"));
        LivePayloadDto payload = LivePayloadDto.builder()
                .city(vars.get("city"))
                .memberUuid(vars.get("memberUuid"))
                .latitude(DELETE)
                .longitude(DELETE)
                .build();
        sendMessageToSessionInCity(payload);
        logger.info(String.format("********** LiveService : %s is disconnected **********", vars.get("memberUuid")));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        Map<String, String> vars = getPathVariable(session);
        liveRedisDao.deleteMemberLocationInCity(vars.get("city"), vars.get("memberUuid"));
        clients.remove(vars.get("memberUuid"));
        LivePayloadDto payload = LivePayloadDto.builder()
                .city(vars.get("city"))
                .memberUuid(vars.get("memberUuid"))
                .latitude(DELETE)
                .longitude(DELETE)
                .build();
        sendMessageToSessionInCity(payload);
        logger.info(String.format("********** LiveService : %s is disconnected by unexpected session failure. **********", vars.get("memberUuid")));
        super.handleTransportError(session, exception);
    }

    private void sendMessageToSessionInCity(LivePayloadDto payload) throws Exception {
        // 1. 유저가 위치한 도시에 존재하는 다른 유저의 id, longitude, latitude 목록 조회
        List<LiveAllLocationsDto> allLocationsInCity = liveRedisDao.getAllLocationsInCity(payload.getCity());
        // 2. 도시에 속한 유저들에게 변경 정보 전송(변경된 유저 아이디, 위치, 거리)
        for (LiveAllLocationsDto otherInfo : allLocationsInCity) {
            if (Objects.equals(otherInfo.getMemberUuid(), payload.getMemberUuid())) continue;
            WebSocketSession memberSession = clients.get(otherInfo.getMemberUuid());
            if (!Objects.isNull(memberSession) && memberSession.isOpen())
                memberSession.sendMessage(
                        new TextMessage(
                                new Gson().toJson(
                                        LiveLocationResDto.builder()
                                                .payload(payload)
                                                .otherLatitude(otherInfo.getLatitude())
                                                .otherLongitude(otherInfo.getLongitude())
                                                .build()))
                );
        }
    }

    private Map<String, String> getPathVariable(WebSocketSession session) {
        Map<String, String> vars = new HashMap<>();
        vars.put("city", (String) session.getAttributes().get("city"));
        vars.put("memberUuid", (String) session.getAttributes().get("memberUuid"));
        return vars;
    }
}