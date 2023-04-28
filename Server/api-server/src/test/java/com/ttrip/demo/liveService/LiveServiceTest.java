package com.ttrip.demo.liveService;


import com.google.gson.Gson;
import com.ttrip.api.dto.Live.LiveLocationResDto;
import com.ttrip.core.TtripApplication;
import com.ttrip.core.customEnum.Gender;
import com.ttrip.core.dto.live.LiveAllLocationsDto;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.liveRedisDao.LiveRedisDao;
import com.ttrip.core.repository.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TtripApplication.class)
@Transactional
@ActiveProfiles("apitest")
public class LiveServiceTest {

    @Autowired
    LiveRedisDao liveRedisDao;
    @Autowired
    MemberRepository memberRepository;

    private static String key = "testCity";

    @AfterEach
    private void initialize() {
        liveRedisDao.deleteMainKey(key);
    }

    @Test
    @DisplayName("live 통신 초기 유저 정보 조회 테스트")
    void LocationInitializationTest(){
        String city = key;

        Double lng = 123.0;
        Double lat = 23.0;
        Map<String, Double> loc = new HashMap<>();
        loc.put("longitude", lng);
        loc.put("latitude", lat);
        UUID mu = UUID.randomUUID();
        Member m = Member.builder().uuid(mu).gender(Gender.MALE).nickname("m").build();
        Member reqM = memberRepository.save(m);

        Double lng1 = 123.1;
        Double lat1 = 23.1;
        Map<String, Double> loc1 = new HashMap<>();
        loc1.put("longitude", lng1);
        loc1.put("latitude", lat1);
        UUID mu1 = UUID.randomUUID();
        Member m1 = Member.builder().uuid(mu1).gender(Gender.MALE).nickname("m1").build();
        Member savedM1 = memberRepository.save(m1);
        liveRedisDao.saveMemberLocationInCity(city, mu1.toString(), loc1);

        Double lng2 = 123.2;
        Double lat2 = 23.2;
        Map<String, Double> loc2 = new HashMap<>();
        loc2.put("longitude", lng2);
        loc2.put("latitude", lat2);
        UUID mu2 = UUID.randomUUID();
        Member m2 = Member.builder().uuid(mu2).gender(Gender.MALE).nickname("m2").build();
        Member savedM2 = memberRepository.save(m2);
        liveRedisDao.saveMemberLocationInCity(city, mu2.toString(), loc2);

        String anotherCity = "anotherCity";
        Double lng3 = 123.3;
        Double lat3 = 23.3;
        Map<String, Double> loc3 = new HashMap<>();
        loc3.put("longitude", lng3);
        loc3.put("latitude", lat3);
        UUID mu3 = UUID.randomUUID();
        Member m3 = Member.builder().uuid(mu3).gender(Gender.MALE).nickname("m3").build();
        Member savedM3 = memberRepository.save(m3);
        liveRedisDao.saveMemberLocationInCity(anotherCity, mu3.toString(), loc3);

        List<LiveAllLocationsDto> locationsInCity = liveRedisDao.getAllLocationsInCity(city);
        List<Member> members = memberRepository
                .findMembersByMemberUuidInOrderByMemberUuid(
                        locationsInCity.stream()
                                .map(sm -> UUID.fromString(sm.getMemberUuid()))
                                .collect(Collectors.toList())
                );
        List<LiveLocationResDto> res = new ArrayList<>();
        for (int i = 0; i < members.size(); i++){
            assertEquals(members.get(i).getMemberUuid().toString(), locationsInCity.get(i).getMemberUuid());
            System.out.println("long : " + locationsInCity.get(i).getLongitude());
            res.add(new LiveLocationResDto(
                    members.get(i), lat, lng, 0,
                    new LiveAllLocationsDto(members.get(i).getMemberUuid().toString(),
                            locationsInCity.get(i).getLongitude(),
                            locationsInCity.get(i).getLatitude()))
            );
            System.out.println(res.get(i).getDistanceFromMe());
        }
        System.out.println("res : " + new Gson().toJson(res));
        assertEquals(2, res.size());
        liveRedisDao.deleteMainKey(anotherCity);
    }
}