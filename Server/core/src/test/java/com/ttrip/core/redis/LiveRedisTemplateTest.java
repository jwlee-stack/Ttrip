package com.ttrip.core.redis;


import com.google.gson.Gson;
import com.ttrip.core.dto.live.LiveAllLocationsDto;
import com.ttrip.core.repository.liveRedisDao.LiveRedisDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LiveRedisTemplateTest {

    @Autowired
    LiveRedisDao liveRedisDao;

    private static String key = "testCity";

    @AfterEach
    private void initialize() {
        liveRedisDao.deleteMainKey(key);
    }

//    @Test
    @DisplayName("LiveRedisTemplate CRUD 기능 테스트")
    void LiveRedisTemplateTest() {
        String city = key;
        String memberUuid = UUID.randomUUID().toString();
        Double longitude = 123.12313123123;
        Double latitude = 23.2134123123;
        Map<String, Double> location = new HashMap<>();
        location.put("longitude", longitude);
        location.put("latitude", latitude);

        liveRedisDao.saveMemberLocationInCity(city, memberUuid, location);
        List<LiveAllLocationsDto> locationWithMemberId = liveRedisDao.getAllLocationsInCity(city);
        assertEquals(1, locationWithMemberId.size());
        assertEquals(memberUuid, locationWithMemberId.get(0).getMemberUuid());
        assertEquals(longitude, locationWithMemberId.get(0).getLongitude());
        assertEquals(latitude, locationWithMemberId.get(0).getLatitude());

        Map<String, Double> loationOnly = liveRedisDao.getCityLocation(city, memberUuid);
        assertEquals(longitude, loationOnly.get("longitude"));
        assertEquals(latitude, loationOnly.get("latitude"));

        Long res = liveRedisDao.deleteMemberLocationInCity(city, memberUuid);
        System.out.println("test : " + res);
        assertEquals(1, res);

        List<LiveAllLocationsDto> emptyResult = liveRedisDao.getAllLocationsInCity(city);
        assertEquals(0, emptyResult.size());
    }

//    @Test
    void SerializationTest() {
        String city = key;
        String memberId = UUID.randomUUID().toString();
        Double longitude = 123.12313123123;
        Double latitude = 23.2134123123;
        Map<String, Double> location = new HashMap<>();
        location.put("longitude", longitude);
        location.put("latitude", latitude);

        liveRedisDao.saveMemberLocationInCity(city, memberId, location);
        List<LiveAllLocationsDto> locationWithMemberId = liveRedisDao.getAllLocationsInCity(city);
        assertEquals(1, locationWithMemberId.size());
        assertEquals(String.format("[{\"memberUuid\":\"%s\",\"longitude\":123.12313123123,\"latitude\":23.2134123123}]", memberId),
                new Gson().toJson(locationWithMemberId));
    }
}
