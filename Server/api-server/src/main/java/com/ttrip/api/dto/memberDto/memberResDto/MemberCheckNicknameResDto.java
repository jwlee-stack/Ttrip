package com.ttrip.api.dto.memberDto.memberResDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@ApiModel(value="닉네임 중복 여부 응답")
public class MemberCheckNicknameResDto {
    @ApiModelProperty(value = "닉네임 중복 여부", notes="true: 중복, false: 중복X", example = "false")
    Boolean isExist;
}
