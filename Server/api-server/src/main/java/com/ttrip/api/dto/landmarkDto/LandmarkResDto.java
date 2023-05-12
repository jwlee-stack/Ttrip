package com.ttrip.api.dto.landmarkDto;

import com.ttrip.core.entity.badge.Landmark;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@ApiModel(value = "랜드마크 추가 응답")
public class LandmarkResDto {
    @ApiModelProperty(value = "랜드마크 아이디", example = "aa.png")
    private Integer landmarkId;
    @ApiModelProperty(value = "랜드마크 이름")
    private String landmarkName;
    @ApiModelProperty(value = "뱃지 사진", notes="파일 형식은 png 또는 jpg", allowEmptyValue = true)
    private String badgeImgPath;
    @ApiModelProperty(value = "랜드마크 위도")
    private Double latitude;
    @ApiModelProperty(value = "랜드마크 경도")
    private Double longitude;

    public static LandmarkResDto toBuild(Landmark landmark) {
        return LandmarkResDto.builder()
                .landmarkId(landmark.getLandmarkId())
                .landmarkName(landmark.getLandmarkName())
                .badgeImgPath(landmark.getBadgeImgPath())
                .latitude(landmark.getLatitude())
                .longitude(landmark.getLongitude())
                .build();
    }
}