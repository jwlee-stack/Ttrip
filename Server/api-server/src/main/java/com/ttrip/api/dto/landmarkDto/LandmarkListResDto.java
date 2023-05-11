package com.ttrip.api.dto.landmarkDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "LandmarkListResDto" ,description = "랜드마크 목록")
public class LandmarkListResDto {
    @ApiModelProperty(value = "랜드마크 Id")
    private Integer landmarkId;
    @ApiModelProperty(value = "랜드마크 이름")
    private String landmarkName;
    @ApiModelProperty(value = "랜드마크 위도")
    private Double latitude;
    @ApiModelProperty(value = "랜드마크 경도")
    private Double longitude;
}