package com.ttrip.api.dto.mypageDto.mypageReqDto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@ApiModel(value = "회원 정보 업데이트 요청")
public class InfoUpdateReqDto {
    private String nickname;
    private Integer age;
    private String gender;
    private String intro;
}
