package com.ttrip.api.dto.memberDto.memberResDto;

import com.ttrip.core.customEnum.Gender;
import com.ttrip.core.entity.member.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@ApiModel(value = "멤버 응답")
public class MemberResDto {
    @ApiModelProperty(value = "UUID", example = "fb71e4a5-bd11-49ae-8d93-62efd2539c20")
    private String uuid;
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

    public static MemberResDto toBuild(Member member)
    {
        return MemberResDto.builder()
                .uuid(member.getMemberUuid().toString())
                .phoneNumber(member.getPhoneNumber())
                .nickname(member.getNickname())
                .intro(member.getIntro())
                .profileImgPath(member.getProfileImgPath())
                .markerImgPath(member.getMarkerImgPath())
                .fcmToken(member.getFcmToken())
                .gender(member.getGender())
                .birthday(member.getBirthday())
                .shareLocation(member.getShareLocation())
                .build();
    }
}
