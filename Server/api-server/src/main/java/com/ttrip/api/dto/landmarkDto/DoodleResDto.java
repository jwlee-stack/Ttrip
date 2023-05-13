package com.ttrip.api.dto.landmarkDto;

import com.ttrip.core.entity.landmark.Doodle;
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
@ApiModel(value = "낙서 저장 응답")
public class DoodleResDto {
    @ApiModelProperty(value = "낙서 Id")
    private Integer doodleId;
    @ApiModelProperty(value = "낙서 위도")
    private Double latitude;
    @ApiModelProperty(value = "낙서 경도")
    private Double longitude;
    @ApiModelProperty(value = "낙서 x좌표")
    private Double positionX;
    @ApiModelProperty(value = "낙서 y좌표")
    private Double positionY;
    @ApiModelProperty(value = "낙서 z좌표")
    private Double positionZ;
    @ApiModelProperty(value = "낙서 사진", notes="파일 형식은 png 또는 jpg", example = "aa.png", allowEmptyValue = true)
    private String doodleImgPath;
    @ApiModelProperty(value = "랜드마크 Id")
    private Integer landmarkId;

    public static DoodleResDto toBuild(Doodle doodle, Integer id) {
        return DoodleResDto.builder()
                .doodleId(id)
                .latitude(doodle.getLatitude())
                .longitude(doodle.getLongitude())
                .positionX(doodle.getPositionX())
                .positionY(doodle.getPositionY())
                .positionZ(doodle.getPositionZ())
                .doodleImgPath(doodle.getDoodleImgPath())
                .landmarkId(doodle.getLandmark().getLandmarkId())
                .build();
    }
}