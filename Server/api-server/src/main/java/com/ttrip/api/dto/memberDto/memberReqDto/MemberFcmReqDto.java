package com.ttrip.api.dto.memberDto.memberReqDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "fcm 토큰 업데이트")
public class MemberFcmReqDto {
    @ApiModelProperty(value = "fcm 토큰")
    private String fcmToken;

}
