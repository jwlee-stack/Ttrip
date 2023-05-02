package com.ttrip.api.dto.mypageDto.mypageReqDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@ApiModel(value = "회원 정보 업데이트 요청")
public class InfoUpdateReqDto {
    @ApiModelProperty(value = "변경할 닉네임")
    private String nickname;
    @ApiModelProperty(value = "변경할 나이", notes="String")
    private String age;
    @ApiModelProperty(value = "변경할 성별")
    private String gender;
    @ApiModelProperty(value = "변경할 소개글")
    private String intro;
}
