package com.ttrip.api.dto.surveyDto.surveyResDto;

import com.ttrip.core.entity.survey.Survey;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SurveyResDto {
    private float preferNatureThanCity;
    private float preferPlan;
    private float preferDirectFlight;
    private float preferCheapHotelThanComfort;
    private float preferGoodFood;
    private float preferCheapTraffic;
    private float preferPersonalBudget;
    private float preferTightSchedule;
    private float preferShoppingThanTour;

    public static SurveyResDto toBuild(Survey survey)
    {
        return SurveyResDto.builder()
                .preferNatureThanCity(survey.getPreferNatureThanCity())
                .preferPlan(survey.getPreferPlan())
                .preferDirectFlight(survey.getPreferDirectFlight())
                .preferCheapHotelThanComfort(survey.getPreferCheapHotelThanComfort())
                .preferGoodFood(survey.getPreferGoodFood())
                .preferCheapTraffic(survey.getPreferCheapTraffic())
                .preferPersonalBudget(survey.getPreferPersonalBudget())
                .preferTightSchedule(survey.getPreferTightSchedule())
                .preferShoppingThanTour(survey.getPreferShoppingThanTour())
                .build();
    }
}
