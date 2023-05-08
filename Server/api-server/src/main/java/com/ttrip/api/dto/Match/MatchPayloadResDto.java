package com.ttrip.api.dto.Match;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchPayloadResDto {
    private boolean isAccess;
    private double latitude;
    private double longitude;
    private double distanceFromMe;
    private boolean isMeet;
}
