package com.ttrip.core.redis;

import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.repository.socketRedisDao.SocketRedisDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class SocketRedisTemplateTest {

    @Autowired
    SocketRedisDao socketRedisDao;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("소켓 레디스 Dao 테스트")
    @Transactional
    void SocketRedisDaoTest() {
        UUID memberUuid = UUID.randomUUID();
        Member member = Member.builder().uuid(memberUuid).build();
        Member savedMember = memberRepository.save(member);

        String zone = ZonedDateTime.now().getZone().toString();
        // long, lat
        Point seoul = new Point(126.9779692, 37.566535);
        socketRedisDao.addLocation(zone, memberUuid.toString(), seoul);

        Point point = new Point(126, 37);
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = socketRedisDao.getNearbyLocations(zone, point, 10000, Metrics.KILOMETERS);
        int count = 0;
        for (GeoResult<RedisGeoCommands.GeoLocation<Object>> result : results) count++;
        assertTrue(count > 0);

        List<Point> points = socketRedisDao.getLocation(zone, memberUuid.toString());
        assertTrue(Math.abs(126.9779692 - points.get(0).getX()) < 0.001 );
        assertTrue(Math.abs(37.566535 - points.get(0).getY()) < 0.001);
        socketRedisDao.removeLocation(zone, memberUuid.toString());
    }
}
