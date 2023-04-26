package com.ttrip.api.dto.memberDto.memberResDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberCheckNicknameResDto {
    Boolean isExist;
}
