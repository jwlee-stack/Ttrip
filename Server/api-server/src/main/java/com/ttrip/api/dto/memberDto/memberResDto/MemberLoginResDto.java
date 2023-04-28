package com.ttrip.api.dto.memberDto.memberResDto;

import com.ttrip.api.dto.tokenDto.TokenDto;
import com.ttrip.core.customEnum.Gender;
import com.ttrip.core.entity.member.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "로그인 응답")
public class MemberLoginResDto {
    @ApiModelProperty(value = "전화번호", example = "01012345678")
    private String phoneNumber;
    @ApiModelProperty(value = "닉네임", example = "진평동대마법사")
    private String nickname;
    @ApiModelProperty(value = "소개말", example = "함께 동행해요^^")
    private String intro;
    @ApiModelProperty(value = "프로필 이미지 경로", notes="파일 이름은 멤버의 uuid", example = "C:\\ssafy\\LastProject\\S08P31D104\\Server\\profileImg\\d6cce70a-e19f-4103-83cf-c8d5a53d5f32.png")
    private String profileImgPath;
    @ApiModelProperty(value = "마커 이미지 경로", notes="파일 이름은 멤버의 uuid", example = "C:\\ssafy\\LastProject\\S08P31D104\\Server\\markerImg\\d6cce70a-e19f-4103-83cf-c8d5a53d5f32.png")
    private String markerImgPath;
    @ApiModelProperty(value = "fcm 토큰", example = "asdfaf79797asdfaf...")
    private String fcmToken;
    @ApiModelProperty(value = "성별", example = "MALE")
    private Gender gender;
    @ApiModelProperty(value = "생일", example = "1995-02-02")
    private LocalDate birthday;
    @ApiModelProperty(value = "위치 정보 활용 동의", notes="기본값: false", example = "false")
    private Boolean shareLocation;
    @ApiModelProperty(value = "액세스 토큰", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMTA3Nzc3IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY4MjU3OTc1Mn0.DJ6cw9gTASIcqsfVvdtLzOUJ-g66JAvfi7HobBstUhFn1IwJuoYUXVLGCQX-z2ykHwlEeKZX_Pgh9D4kn2OJZw")
    private TokenDto tokenDto;

    public static MemberLoginResDto toBuild(Member member, TokenDto tokenDto)
    {
        return MemberLoginResDto.builder()
                .phoneNumber(member.getPhoneNumber())
                .nickname(member.getNickname())
                .intro(member.getIntro())
                .profileImgPath(member.getProfileImgPath())
                .markerImgPath(member.getMarkerImgPath())
                .fcmToken(member.getFcmToken())
                .gender(member.getGender())
                .birthday(member.getBirthday())
                .shareLocation(member.getShareLocation())
                .tokenDto(tokenDto)
                .build();
    }
}
