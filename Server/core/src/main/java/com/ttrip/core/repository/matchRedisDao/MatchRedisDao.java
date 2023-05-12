package com.ttrip.core.repository.matchRedisDao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class MatchRedisDao {
    private final RedisTemplate<String, Object> matchRedisTemplate;
    private HashOperations<String, String, Map<String, Double>> mapHashOperations;
    @PostConstruct
    private void init() {
        mapHashOperations = matchRedisTemplate.opsForHash();
    }

    public MatchRedisDao(@Qualifier("matchRedisTemplate") RedisTemplate<String, Object> matchRedisTemplate) {
        this.matchRedisTemplate = matchRedisTemplate;
    }

    public void saveMemberLocation(String memberUuid, Map<String, Double> location) {
        mapHashOperations.put(memberUuid, "match", location);
    }

    public void deleteMemberLocation(String memberUuid) {
        mapHashOperations.delete(memberUuid, "match");
    }

    public Map<String, Double> getMemberLocation(String memberUuid) {
        System.out.println(mapHashOperations.get(memberUuid, "match").get("latitude"));
        return mapHashOperations.get(memberUuid, "match");
    }
}
