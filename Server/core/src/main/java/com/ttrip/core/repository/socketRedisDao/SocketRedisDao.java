package com.ttrip.core.repository.socketRedisDao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SocketRedisDao {
    private final RedisTemplate<String, Object> socketRedisTemplate;

    public SocketRedisDao(
            @Qualifier("socketRedisTemplate") RedisTemplate<String, Object> socketRedisTemplate) {
        this.socketRedisTemplate = socketRedisTemplate;
    }

    /**
     * 유저별 위치 저장
     *
     * @param city
     * @param memberId
     * @param point
     */
    public void addLocation(String city, String memberId, Point point) {
        String longitude = String.format("%.12f", point.getX());
        String latitude = String.format("%.12f", point.getY());
        socketRedisTemplate.opsForGeo().add(city, new Point(Double.parseDouble(longitude), Double.parseDouble(latitude)), memberId);
    }

    /**
     * 특정 국가의 위치를 중심으로 반경 내에 포함되는 위치 반환
     *
     * @param city
     * @param point   : 중심점의 좌표 객체
     * @param radius  : 검색할 반경
     * @param metrics : 반경의 단위 열거형 :  KILOMETERS, MILES 등
     *                1. 반경과 단위를 사용하여 Distance 객체를 생성합니다.
     *                2. 중심점의 좌표와 거리를 사용하여 검색 영역을 나타내는 Circle 객체를 생성합니다.
     *                3. GeoRadius 검색에 사용될 명령 인수를 생성합니다. 이 경우, 거리, 좌표를 포함하고 오름차순으로 정렬하는 옵션을 설정합니다.
     *                4. RedisTemplate의 opsForGeo() 메서드를 사용하여 Geo 관련 작업을 수행할 수 있는 GeoOperations 인터페이스를 얻습니다.
     *                radius() 메서드를 호출하여 주어진 영역 내의 위치를 검색
     * @return GeoResults<RedisGeoCommands.GeoLocation < Object>> : Redis GeoSpatial 데이터 조회 결과를 표현하는 Spring Data Redis 클래스
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> getNearbyLocations(String city, Point point, double radius, Metrics metrics) {
        Distance distance = new Distance(radius, metrics);
        Circle circle = new Circle(point, distance);

        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending();
        return socketRedisTemplate.opsForGeo().radius(city, circle, args);
    }

    public void removeLocation(String city, String memberId) {
        socketRedisTemplate.opsForGeo().remove(city, memberId);
    }

    public Distance getDistanceBetween(String city, String fromMemberId, String toMemberId) {
        return socketRedisTemplate.opsForGeo().distance(city, fromMemberId, toMemberId);
    }

    public List<Point> getLocation(String city, String memberId) {
        return socketRedisTemplate.opsForGeo().geoPos(city, memberId);
    }
}
