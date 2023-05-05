package com.ttrip.core.repository.surveyRedisDao;

import com.google.gson.Gson;
import com.ttrip.core.dto.live.LiveAllLocationsDto;
import com.ttrip.core.repository.survey.SurveyRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class SurveyRedisDao {
    private final RedisTemplate<String, Object> surveyRedisTemplate;
    private HashOperations<String, String, String> surveyHashOperations;
    private final String SURVEY_KEY = "survey";
    @PostConstruct
    private void init() {
        surveyHashOperations = surveyRedisTemplate.opsForHash();
    }

    public SurveyRedisDao(@Qualifier("surveyRedisTemplate") RedisTemplate<String, Object> surveyRedisTemplate) {
        this.surveyRedisTemplate = surveyRedisTemplate;
    }

    public void saveSurveyCache(String memberUuid, double[] vector){
        surveyHashOperations.put(SURVEY_KEY, memberUuid, new Gson().toJson(vector));
    }
    public double[] getSurveyCache(String memberUuid){
        return new Gson().fromJson(surveyHashOperations.get(SURVEY_KEY, memberUuid), double[].class);
    }
}
