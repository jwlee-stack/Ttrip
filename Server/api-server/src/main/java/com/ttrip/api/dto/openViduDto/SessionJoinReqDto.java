package com.ttrip.api.dto.openViduDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="토큰 발급")
public class SessionJoinReqDto {
    @ApiModelProperty(value = "세션 아이디", example = "sessionA")
    private String sessionId;
    @ApiModelProperty(value = "사용자 uuid", example = "fb71e4a5-bd11-...")
    private String uuid;
}