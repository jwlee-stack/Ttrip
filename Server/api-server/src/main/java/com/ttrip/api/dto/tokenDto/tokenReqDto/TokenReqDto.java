package com.ttrip.api.dto.tokenDto.tokenReqDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenReqDto {
    private String accessToken;
    private String refreshToken;
}

