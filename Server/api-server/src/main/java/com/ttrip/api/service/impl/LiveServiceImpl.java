package com.ttrip.api.service.impl;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.Live.LiveLocationResDto;
import com.ttrip.api.exception.NotFoundException;
import com.ttrip.api.service.LiveService;
import com.ttrip.core.dto.live.LiveAllLocationsDto;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.entity.survey.Survey;
import com.ttrip.core.repository.liveRedisDao.LiveRedisDao;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.repository.survey.SurveyRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
import com.ttrip.core.utils.EuclideanDistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LiveServiceImpl implements LiveService {

    private final LiveRedisDao liveRedisDao;
    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final EuclideanDistanceUtil rateUtil;

    /**
     * 유저의 정보와 위치 정보를 결합한 데이터들이 반환됩니다.
     * @param city : 조회할 도시명
     * @param lng : 조회하는 유저의 longitude
     * @param lat : 조회하는 유저의 latitude
     * @return : nickname, gender, age, memberUuid, latitude, longitude, matchingRate, distanceFromMe 데이터를 담은 리스트
     */
    public DataResDto<?> getMembersInCity(Member requester, String city, double lng, double lat){
        List<LiveAllLocationsDto> locationsInCity = liveRedisDao.getAllLocationsInCity(city);
        List<LiveLocationResDto> res = new ArrayList<>();
        if (locationsInCity.isEmpty())
            return DataResDto.builder()
                    .message(String.format("%s에 존재하는 유저 정보 목록입니다.", city))
                    .data(res)
                    .build();
        Survey requesterSurvey = surveyRepository.findByMember(requester)
                .orElseThrow(() -> new NotFoundException(ErrorMessageEnum.SURVEY_REQUIRED.getMessage()));
        List<Member> others = memberRepository
                .findMembersByMemberUuidInOrderByMemberUuid(
                        locationsInCity.stream()
                                .map(m -> UUID.fromString(m.getMemberUuid()))
                                .collect(Collectors.toList())
                );
        for (int i = 0; i < others.size(); i++){
            Member other = others.get(i);
            if (!Objects.equals(other.getMemberUuid().toString(), locationsInCity.get(i).getMemberUuid())
                    || Objects.equals(other.getMemberUuid().toString(), requester.getMemberUuid().toString()))
                continue;
            res.add(new LiveLocationResDto(other, lat, lng, rateUtil.getMatchingRate(requesterSurvey, other.getSurvey()), locationsInCity.get(i)));
        }
        return DataResDto.builder()
                .message(String.format("%s에 존재하는 유저 정보 목록입니다.", city))
                .data(res)
                .build();
    }
}