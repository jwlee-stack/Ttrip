package com.ttrip.core.repository.liveRedisDao;

import com.ttrip.core.dto.live.LiveAllLocationsDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class LiveRedisDao {
    private final RedisTemplate<String, Object> liveRedisTemplate;
    private HashOperations<String, String, Map<String, Double>> hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = liveRedisTemplate.opsForHash();
    }

    public LiveRedisDao(
            @Qualifier("liveRedisTemplate") RedisTemplate<String, Object> liveRedisTemplate) {
        this.liveRedisTemplate = liveRedisTemplate;
    }

    public void saveMemberLocationInCity(String city, String memberUuid, Map<String, Double> location) {
        hashOperations.put(city, memberUuid, location);
    }

    public long deleteMemberLocationInCity(String city, String memberUuid) {
        return hashOperations.delete(city, memberUuid);
    }

    public Map<String, Double> getCityLocation(String city, String memberUuid) {
        return hashOperations.get(city, memberUuid);
    }

    public List<LiveAllLocationsDto> getAllLocationsInCity(String city) {
        Set<String> memberUuids = getMemberUuidsInCity(city);
        return memberUuids.stream()
                .map(memberUuid -> {
                    Map<String, Double> location = hashOperations.get(city, memberUuid);
                    return LiveAllLocationsDto.builder().memberUuid(memberUuid).location(location).build();
                })
                .collect(Collectors.toList());
    }
    public void deleteMainKey(String mainKey) {
        liveRedisTemplate.delete(mainKey);
    }
    public Set<String> getMemberUuidsInCity(String city){
        return hashOperations.keys(city);

    }
}
