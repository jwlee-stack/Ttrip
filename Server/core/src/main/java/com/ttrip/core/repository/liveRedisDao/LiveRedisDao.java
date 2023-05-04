package com.ttrip.core.repository.liveRedisDao;

import com.ttrip.core.dto.live.LiveAllLocationsDto;
import com.ttrip.core.repository.survey.SurveyRepository;
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
    private final RedisTemplate<String, Object> surveyRedisTemplate;
    private HashOperations<String, String, Map<String, Double>> liveHashOperations;
    private HashOperations<String, String, double[]> surveyHashOperations;
    private final String SURVEY_KEY = "survey";
    private final SurveyRepository surveyRepository;
    @PostConstruct
    private void init() {
        liveHashOperations = liveRedisTemplate.opsForHash();
        surveyHashOperations = surveyRedisTemplate.opsForHash();
    }

    public LiveRedisDao(
            @Qualifier("liveRedisTemplate") RedisTemplate<String, Object> liveRedisTemplate,
            @Qualifier("surveyRedisTemplate") RedisTemplate<String, Object> surveyRedisTemplate,
            SurveyRepository surveyRepository) {
        this.liveRedisTemplate = liveRedisTemplate;
        this.surveyRedisTemplate = surveyRedisTemplate;
        this.surveyRepository = surveyRepository;
    }

    public void saveMemberLocationInCity(String city, String memberUuid, Map<String, Double> location) {
        liveHashOperations.put(city, memberUuid, location);
    }

    public long deleteMemberLocationInCity(String city, String memberUuid) {
        return liveHashOperations.delete(city, memberUuid);
    }

    public Map<String, Double> getCityLocation(String city, String memberUuid) {
        return liveHashOperations.get(city, memberUuid);
    }

    /**
     * 특정 도시에 속한 유저의 위치 정보를 uuid기준으로 정렬하여 반환합니다.
     * @param city : 조회한 도시명
     * @return : memberUuid, longitude, latitude 정보를 담은 리스트가 반환됩니다.
     */
    public List<LiveAllLocationsDto> getAllLocationsInCity(String city) {
        Set<String> memberUuids = getMemberUuidsInCity(city);
        List<String> sortedMemberUuids = memberUuids.stream()
                .sorted()
                .collect(Collectors.toList());
        return sortedMemberUuids.stream()
                .map(memberUuid -> {
                    Map<String, Double> location = liveHashOperations.get(city, memberUuid);
                    return LiveAllLocationsDto.builder().memberUuid(memberUuid).location(location).build();
                })
                .collect(Collectors.toList());
    }
    public void deleteMainKey(String mainKey) {
        liveRedisTemplate.delete(mainKey);
    }
    public Set<String> getMemberUuidsInCity(String city){
        return liveHashOperations.keys(city);
    }
    public void saveSurveyCache(String memberUuid, double[] vector){
        surveyHashOperations.put(SURVEY_KEY, memberUuid, vector);
    }
    public double[] getSurveyCache(String memberUuid){
        return surveyHashOperations.get(SURVEY_KEY, memberUuid);
    }
}
