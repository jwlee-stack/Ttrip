package com.ttrip.api.service.impl;

import com.ttrip.api.dto.*;
import com.ttrip.api.dto.openViduDto.SessionJoinReqDto;
import com.ttrip.api.dto.openViduDto.SessionResDto;
import com.ttrip.api.service.OpenViduService;
import com.ttrip.core.repository.openVidu.OpenViduRedisDao;
import com.ttrip.core.utils.ErrorMessageEnum;
import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.ConnectionType;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.Session;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class OpenViduServiceImpl implements OpenViduService {
    private static final Logger logger = LogManager.getLogger(OpenViduServiceImpl.class);
    OpenViduRedisDao openViduRedisDao;

    private OpenVidu openVidu;

    @Value("${openvidu.url}")
    private String OPENVIDU_URL;

    @Value("${openvidu.secret}")
    private String OPENVIDU_SECRET;

    @PostConstruct
    public void init() {
        this.openVidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    /**
     * 세션이 생성됩니다.
     * @return : 생성된 세션의 ID
     */
    @Override
    public DataResDto<?> createSession() throws OpenViduJavaClientException, OpenViduHttpException {
        Session newSession = openVidu.createSession();

        SessionResDto sessionId = SessionResDto.builder().sessionId(newSession.getSessionId()).build();
        return DataResDto.builder()
                .message("세션이 생성되었습니다.")
                .data(sessionId)
                .build();
    }

    /**
     * 세션에 입장합니다.
     * @param sessionJoinReqDto : (sessionId, nickname)
     * @return : 생성된 토큰
     */
    @Override
    public DataResDto<?> joinSession(SessionJoinReqDto sessionJoinReqDto) throws OpenViduJavaClientException, OpenViduHttpException {
        String token = null;
        // 세션이 존재하지 않을 때
        if (!openViduRedisDao.findSessionId(sessionJoinReqDto.getSessionId())) {
            // 토큰 발급
            token = createOpenViduToken(sessionJoinReqDto.getSessionId(), sessionJoinReqDto.getUuid());
            // 저장
            openViduRedisDao.saveOpenViduSession(sessionJoinReqDto.getSessionId(), sessionJoinReqDto.getUuid());
        } else {
            // 이미 세션에 2명이 들어가 있을 때
            if (!openViduRedisDao.JoinableSession(sessionJoinReqDto.getSessionId())) {
                throw new RuntimeException(ErrorMessageEnum.ALREADY_CALLING.getMessage());
            } else {
                // 토큰 발급
                token = createOpenViduToken(sessionJoinReqDto.getSessionId(), sessionJoinReqDto.getUuid());
                // 저장
                openViduRedisDao.saveOpenViduSession(sessionJoinReqDto.getSessionId(), sessionJoinReqDto.getUuid());
            }
        }
        if(token == null)
            throw new RuntimeException(ErrorMessageEnum.FAILD_TO_TOKEN.getMessage());

        return DataResDto.builder()
                .message("토큰이 발급되었습니다.")
                .data(token)
                .build();
    }

    /**
     * 토큰을 발급합니다.
     * @param sessionId : OpenVidu SessionId
     * @param uuid : 사용자 uuid
     * @return : 생성된 토큰
     */
    private String createOpenViduToken(String sessionId, String uuid) throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openVidu.getActiveSession(sessionId);
        ConnectionProperties properties = new ConnectionProperties.Builder()
                .type(ConnectionType.WEBRTC)
                .role(OpenViduRole.SUBSCRIBER)
                .data("{\"memberUuid\": \"" + uuid + "\"}")
                .build();
        // ex) wss://localhost:4443?sessionId=ses_Ogize1yQIj&token=tok_A1c0pNsLJFwVJTeb
        return session.createConnection(properties).getToken();
    }
}