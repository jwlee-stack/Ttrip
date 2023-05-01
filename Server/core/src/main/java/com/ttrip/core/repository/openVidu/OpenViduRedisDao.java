package com.ttrip.core.repository.openVidu;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OpenViduRedisDao {
    private final RedisTemplate<String, Object> openviduRedisTemplate;
    private SetOperations<String, Object> setOperations;

    @PostConstruct
    private void init() {
        setOperations = openviduRedisTemplate.opsForSet();
    }

    public OpenViduRedisDao(
            @Qualifier("openviduRedisTemplate") RedisTemplate<String, Object> openviduRedisTemplate) {
        this.openviduRedisTemplate = openviduRedisTemplate;
    }

    /**
     * session에 유저들을 저장합니다.
     * @param sessionId : OpenVidu SessionId
     * @param uuid : 세션에 합류하는 사용자 uuid
     */
    public void saveOpenViduSession(String sessionId, String uuid) {
        setOperations.add("openvidu:" + sessionId, uuid);
    }

    /**
     * Redis에 해당 세션이 있는지 확인합니다.
     * @param sessionId : OpenVidu SessionId
     * @return : 해당 세션이 존재하면 true, 존재하지 않으면 false를 반환합니다.
     */
    public boolean findSessionId(String sessionId) {
        return openviduRedisTemplate.hasKey("openvidu:" + sessionId);
    }

    /**
     * 들어갈 수 있는 세션인지 판단합니다.
     * @param sessionId : OpenVidu SessionId
     * @return : 들어갈 수 있는 세션이라면 true, 들어갈 수 없다면 false를 반환합니다.
     */
    public boolean JoinableSession(String sessionId) {
        if(setOperations.size(sessionId) < 2)
            return true;
        return false;
    }
}