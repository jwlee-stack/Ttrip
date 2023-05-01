package com.ttrip.api.dto.surveyDto.surveyResDto;

import com.ttrip.core.entity.survey.Survey;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SurveyResDto {
    private int preferNatureThanCity;
    private int preferPlan;
    private int preferDirectFlight;
    private int preferCheapHotelThanComfort;
    private int preferGoodFood;
    private int preferCheapTraffic;
    private int preferPersonalBudget;
    private int preferTightSchedule;
    private int preferShoppingThanTour;

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
