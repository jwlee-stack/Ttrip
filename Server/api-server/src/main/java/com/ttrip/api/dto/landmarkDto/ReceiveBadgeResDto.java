package com.ttrip.api.dto.landmarkDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@ApiModel(value = "ReceiveBadgeReqDto", description = "뱃지 발급 dto")
public class ReceiveBadgeResDto {
    @ApiModelProperty(value = "뱃지 id")
    private Integer badgeId;
    @ApiModelProperty(value = "뱃지(랜드마크) 이름")
    private String badgeName;
    @ApiModelProperty(value = "뱃지 이미지 경로", example = "C:\\ssafy\\Project\\S08P31D104\\Server\\badgeImg\\landmark.png")
    private String badgeImgPath;
}