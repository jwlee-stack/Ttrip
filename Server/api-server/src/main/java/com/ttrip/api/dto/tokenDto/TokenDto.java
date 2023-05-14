package com.ttrip.api.dto.tokenDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "JWT 토큰")
public class TokenDto {
    @ApiModelProperty(value = "인증 타입", example = "Bearer")
    private String grantType;
    @ApiModelProperty(value = "액세스 토큰", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMTA3ODc4IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4MjY2NzAyMX0.DNspwN-lFiz69pnkvxcAI2tqYldwNPlXZvAhvPwE3D6l_oejHOM0ki4PqWCQsPoq9TKqcba5PN4TwB7QCa6ImQ")
    private String accessToken;
    @ApiModelProperty(value = "리프래시 토큰", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2ODMxODU0MjF9.nifCpn1us7kBLL1EZIfkg1B9EsMEIoz1NhVyLzENbKeExwRV0fWlCej2Z422bhrcmitKa6uVK0TbkK9h7bGyXA")
    private String refreshToken;
    @ApiModelProperty(value = "토큰 만료 시간", example = "1682667021094")
    private Long accessTokenExpiresIn;
    @ApiModelProperty(value = "유저 닉네임", example = "jwlee")
    private String nickname;
}

