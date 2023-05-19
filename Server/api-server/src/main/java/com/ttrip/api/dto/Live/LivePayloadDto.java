package com.ttrip.api.dto.Live;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LivePayloadDto {
    private String nickname;
    private String gender;
    private String age;
    private String city;
    private String memberUuid;
    private double latitude;
    private double longitude;
    private String profileImgPath;
    private String markerImgPath;

    @Builder
    public LivePayloadDto(String nickname, String gender, String age, String city, String memberUuid, double latitude, double longitude, String profileImgPath, String markerImgPath) {
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.city = city;
        this.memberUuid = memberUuid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profileImgPath = profileImgPath;
        this.markerImgPath = markerImgPath;
    }
}
