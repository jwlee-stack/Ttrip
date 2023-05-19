package com.ttrip.api.dto.surveyDto.surverReqDto;

import lombok.Data;

@Data
public class SurveyReqDto {
    private int preferNatureThanCity;
    private int preferPlan;
    private int preferDirectFlight;
    private int preferCheapHotelThanComfort;
    private int preferGoodFood;
    private int preferCheapTraffic;
    private int preferPersonalBudget;
    private int preferTightSchedule;
    private int preferShoppingThanTour;
}
