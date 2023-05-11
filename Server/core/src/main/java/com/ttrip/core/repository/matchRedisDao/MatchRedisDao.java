package com.ttrip.core.repository.matchRedisDao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class MatchRedisDao {
    private final RedisTemplate<String, Map<String, Double>> matchRedisTemplate;
    private SetOperations<String, Map<String, Double>> matchSetOperations;

    @PostConstruct
    private void init() {
        matchSetOperations = matchRedisTemplate.opsForSet();
    }

    public MatchRedisDao(@Qualifier("matchRedisTemplate") RedisTemplate<String, Map<String, Double>> matchRedisTemplate) {
        this.matchRedisTemplate = matchRedisTemplate;
    }

    public void saveMemberLocation(String memberUuid, Map<String, Double> location) {
        matchSetOperations.add(memberUuid, location);
    }

    public void deleteMemberLocation(String memberUuid) {
        matchSetOperations.pop(memberUuid);
    }

    public Map<String, Double> getMemberLocation(String memberUuid) {
        return matchRedisTemplate.opsForValue().get(memberUuid);
    }
}
