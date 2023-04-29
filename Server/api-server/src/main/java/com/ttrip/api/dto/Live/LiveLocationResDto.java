package com.ttrip.api.dto.Live;

import com.ttrip.core.dto.live.LiveAllLocationsDto;
import com.ttrip.core.entity.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LiveLocationResDto {
    private String nickname;
    private String gender;
    private String age;
    private String memberUuid;
    private double latitude;
    private double longitude;
    private double matchingRate;
    private String profileImgPath;
    private String markerImgPath;
    private double distanceFromMe;

    @Builder
    public LiveLocationResDto(LivePayloadDto payload, double matchingRate, double otherLatitude, double otherLongitude) {
        this.nickname = payload.getNickname();
        this.gender = payload.getGender();
        this.age = payload.getAge();
        this.memberUuid = payload.getMemberUuid();
        this.longitude = payload.getLongitude();
        this.latitude = payload.getLatitude();
        this.matchingRate = matchingRate;
        this.profileImgPath = payload.getProfileImgPath();
        this.markerImgPath = payload.getMarkerImgPath();
        this.distanceFromMe =
                Objects.equals(payload.getLatitude(), -1) ?
                        0 :
                        getDistance(payload.getLatitude(), payload.getLongitude(), otherLatitude, otherLongitude);
    }
    public LiveLocationResDto(Member other, Double memberLat, Double memberLng,
                              double matchingRate, LiveAllLocationsDto otherLocation) {
        this.nickname = other.getNickname();
        this.gender = Objects.isNull(other.getGender()) ? "null" : other.getGender().toString();
        this.age = Objects.isNull(other.getBirthday()) ? "null" : other.getBirthday().toString();
        this.memberUuid = other.getMemberUuid().toString();
        this.longitude = otherLocation.getLongitude();
        this.latitude = otherLocation.getLatitude();
        this.matchingRate = matchingRate;
        this.profileImgPath = other.getProfileImgPath();
        this.markerImgPath = other.getMarkerImgPath();
        this.distanceFromMe = getDistance(memberLat, memberLng, otherLocation.getLongitude(), otherLocation.getLatitude());
    }

    /**
     * 하버사인 공식(Haversine formula) : 두 점 사이의 대원거리를 계산합니다.
     *
     * @param lat1 : 한 점의 위도
     * @param lon1 : 한 점의 경도
     * @param lat2 : 다른 점의 위도
     * @param lon2 : 다른 점의 경도
     * @return : 두 점의 km 단위 거리
     */
    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        int EARTH_RADIUS = 6371; // 지구 반지름 (킬로미터 단위)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(EARTH_RADIUS * c);
    }
}
