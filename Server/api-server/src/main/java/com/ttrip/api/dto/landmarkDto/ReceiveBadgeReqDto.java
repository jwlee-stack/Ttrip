package com.ttrip.api.dto.landmarkDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ReceiveBadgeReqDto", description = "뱃지 발급 dto")
public class ReceiveBadgeReqDto {
    @ApiModelProperty(value = "랜드마크 id")
    private Integer landmarkId;
}