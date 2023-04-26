package com.ttrip.api.dto.memberDto.memberResponseDto;

import com.ttrip.api.dto.tokenDto.TokenDto;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.enum2.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginResponseDto {
    //private Member member;
    private String phoneNumber;
    private String nickname;
    private String intro;
    private String imagePath;
    private String fcmToken;
    private Gender gender;
    private LocalDate birthday;
    private Boolean shareLocation;
    private TokenDto tokenDto;


    public static MemberLoginResponseDto toBuild(Member member, TokenDto tokenDto)
    {
        return MemberLoginResponseDto.builder()
                .phoneNumber(member.getPhoneNumber())
                .nickname(member.getNickname())
                .intro(member.getIntro())
                .imagePath(member.getImagePath())
                .fcmToken(member.getFcmToken())
                .gender(member.getGender())
                .birthday(member.getBirthday())
                .shareLocation(member.getShareLocation())
                .tokenDto(tokenDto)
                .build();
    }
}
