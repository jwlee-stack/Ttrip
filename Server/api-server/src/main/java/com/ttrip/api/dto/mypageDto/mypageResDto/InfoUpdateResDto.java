package com.ttrip.api.dto.mypageDto.mypageResDto;

import com.ttrip.core.entity.member.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@ApiModel(value = "회원 정보 업데이트 응답")
public class InfoUpdateResDto {
    @ApiModelProperty(value = "닉네임", example = "진평동대마법사")
    private String nickname;
    @ApiModelProperty(value = "나이", example = "23")
    private Integer age;
    @ApiModelProperty(value = "성별", example = "MALE")
    private String gender;
    @ApiModelProperty(value = "소개말", example = "함께 동행해요^^")
    private String intro;

    public static InfoUpdateResDto toBuild(Member member)
    {
        return InfoUpdateResDto.builder()
                .nickname(member.getNickname())
                .intro(member.getIntro())
                .gender(member.getGender().toString())
                .age(member.getAge())
                .build();
    }


}
