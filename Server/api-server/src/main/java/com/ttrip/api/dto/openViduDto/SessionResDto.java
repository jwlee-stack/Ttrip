package com.ttrip.api.dto.openViduDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value="세션 초기화")
public class SessionResDto {
    @ApiModelProperty(value = "세션 초기화", example = "sessionA")
    private String sessionId;
}