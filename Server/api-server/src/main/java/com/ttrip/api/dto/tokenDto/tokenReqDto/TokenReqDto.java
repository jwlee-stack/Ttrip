package com.ttrip.api.dto.tokenDto.tokenReqDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "토큰 재발행")
public class TokenReqDto {
    @ApiModelProperty(value = "현재 액세스 토큰")
    private String accessToken;
    @ApiModelProperty(value = "리프래시 토큰")
    private String refreshToken;
}

