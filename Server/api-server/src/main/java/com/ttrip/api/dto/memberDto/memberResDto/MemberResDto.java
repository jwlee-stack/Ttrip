package com.ttrip.api.dto.memberDto.memberResDto;

import com.ttrip.core.customEnum.Gender;
import com.ttrip.core.entity.member.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MemberResDto {
    private String phoneNumber;
    private String nickname;
    private String intro;
    private String imagePath;
    private String fcmToken;
    private Gender gender;
    private LocalDate birthday;
    private Boolean shareLocation;

    public static MemberResDto toBuild(Member member)
    {
        return MemberResDto.builder()
                .phoneNumber(member.getPhoneNumber())
                .nickname(member.getNickname())
                .intro(member.getIntro())
                .imagePath(member.getProfileImgPath())
                .fcmToken(member.getFcmToken())
                .gender(member.getGender())
                .birthday(member.getBirthday())
                .shareLocation(member.getShareLocation())
                .build();
    }
}
