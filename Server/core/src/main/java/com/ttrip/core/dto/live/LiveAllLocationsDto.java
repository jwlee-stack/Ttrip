package com.ttrip.core.dto.live;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LiveAllLocationsDto {
    private String memberUuid;
    private Double longitude;
    private Double latitude;

    @Builder
    public LiveAllLocationsDto(String memberUuid, Map<String, Double> location) {
        this.memberUuid = memberUuid;
        this.longitude = location.get("longitude");
        this.latitude = location.get("latitude");
    }
}
