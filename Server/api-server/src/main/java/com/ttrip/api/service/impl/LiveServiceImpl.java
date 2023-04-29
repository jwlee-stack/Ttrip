package com.ttrip.api.service.impl;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.Live.LiveLocationResDto;
import com.ttrip.api.service.LiveService;
import com.ttrip.core.dto.live.LiveAllLocationsDto;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.liveRedisDao.LiveRedisDao;
import com.ttrip.core.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LiveServiceImpl implements LiveService {

    private final LiveRedisDao liveRedisDao;
    private final MemberRepository memberRepository;

    /**
     * 유저의 정보와 위치 정보를 결합한 데이터들이 반환됩니다.
     * @param city : 조회할 도시명
     * @param lng : 조회하는 유저의 longitude
     * @param lat : 조회하는 유저의 latitude
     * @return : nickname, gender, age, memberUuid, latitude, longitude, matchingRate, distanceFromMe 데이터를 담은 리스트
     */
    public DataResDto<?> getMembersInCity(String city, double lng, double lat){
        List<LiveAllLocationsDto> locationsInCity = liveRedisDao.getAllLocationsInCity(city);
        List<Member> members = memberRepository
                .findMembersByMemberUuidInOrderByMemberUuid(
                        locationsInCity.stream()
                                .map(m -> UUID.fromString(m.getMemberUuid()))
                                .collect(Collectors.toList())
                );
        List<LiveLocationResDto> res = new ArrayList<>();
        for (int i = 0; i < members.size(); i++){
            if (!Objects.equals(members.get(i).getMemberUuid().toString(), locationsInCity.get(i).getMemberUuid()))
                continue;
            res.add(new LiveLocationResDto(members.get(i), lat, lng, 0, locationsInCity.get(i)));
        }
        return DataResDto.builder()
                .message(String.format("%s에 존재하는 유저 정보 목록입니다.", city))
                .data(res)
                .build();
    }
}
