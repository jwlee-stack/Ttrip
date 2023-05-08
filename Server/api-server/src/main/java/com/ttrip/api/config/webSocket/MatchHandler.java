package com.ttrip.api.config.webSocket;

import com.google.gson.Gson;
import com.ttrip.api.dto.Match.MatchPayloadReqDto;
import com.ttrip.api.dto.Match.MatchPayloadResDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class MatchHandler extends TextWebSocketHandler {
    //유저 위치 레디스로 이사 예정
    private final static ConcurrentHashMap<String, WebSocketSession> clients = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, Double> latitude = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, Double> longitude = new ConcurrentHashMap<>();
    private final Logger logger = LogManager.getLogger(ChatHandler.class);

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String memberUuid = session.getAttributes().get("memberUuid").toString();
        String opponentUuid = session.getAttributes().get("opponentUuid").toString();
        Gson gson = new Gson();
        MatchPayloadReqDto matchPayloadDto = gson.fromJson(message.getPayload(), MatchPayloadReqDto.class);

        latitude.put(memberUuid, matchPayloadDto.getLatitude());
        longitude.put(memberUuid, matchPayloadDto.getLongitude());

        //상대 접속  안했으면 보낸 사용자에게 false 반환
        if (Objects.isNull(clients.get(opponentUuid))) {
            clients.get(memberUuid).sendMessage(new TextMessage(gson.toJson(MatchPayloadResDto.builder()
                    .isAccess(false)
                    .build())));
        }else {
            //접속했다면 거리 계산
            clients.get(opponentUuid).sendMessage(new TextMessage(gson.toJson(MatchPayloadResDto.builder()
                    .isAccess(true)
                    .latitude(matchPayloadDto.getLatitude())
                    .longitude(matchPayloadDto.getLongitude())
                    .distanceFromMe(getDistance(matchPayloadDto.getLatitude(),
                            matchPayloadDto.getLongitude(),
                            latitude.get(opponentUuid),
                            longitude.get(opponentUuid)))
                    .isMeet(matchPayloadDto.getIsMeet())
                    .build()
            )));
            logger.info("match: " + memberUuid + " send to " + opponentUuid);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        clients.put(session.getAttributes().get("memberUuid").toString(), session);
        logger.info("chatting: {} access", session.getAttributes().get("memberUuid").toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // 정보 제거
        clients.remove(session.getAttributes().get("memberUuid").toString());
        latitude.remove(session.getAttributes().get("memberUuid").toString());
        longitude.remove(session.getAttributes().get("memberUuid").toString());
        logger.info("chatting: {} exit", session.getAttributes().get("memberUuid").toString());
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        System.out.println(lat1 + " " + lon1 + " " + lat2 + " " + lon2);
        int EARTH_RADIUS = 6371; // 지구 반지름 (킬로미터 단위)
        double dLat = Math.toRadians(Math.abs(lat2 - lat1));
        double dLon = Math.toRadians(Math.abs(lon2 - lon1));

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        logger.info("distance: {} ", Math.round(EARTH_RADIUS * c));
        return Math.round(EARTH_RADIUS * c);
    }
}
