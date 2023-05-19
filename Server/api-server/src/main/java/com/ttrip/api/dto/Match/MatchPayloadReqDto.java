package com.ttrip.api.dto.Match;

import lombok.Data;

@Data
public class MatchPayloadReqDto {
    private double latitude;
    private double longitude;
    private Boolean isMeet;
}
