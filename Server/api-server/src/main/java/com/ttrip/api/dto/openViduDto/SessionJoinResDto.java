package com.ttrip.api.dto.openViduDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value="토큰 발급")
public class SessionJoinResDto {
    @ApiModelProperty(value = "OpenVidu 토큰", example = "wss://localhost:4443?sessionId=ses...&token=tok...")
    private String openViduToken;
}