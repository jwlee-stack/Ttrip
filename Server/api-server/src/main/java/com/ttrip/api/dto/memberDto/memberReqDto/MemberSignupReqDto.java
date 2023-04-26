package com.ttrip.api.dto.memberDto.memberReqDto;

import com.ttrip.core.entity.member.Member;
import com.ttrip.core.customEnum.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignupReqDto {
    private String phoneNumber;
    private String password;
    private Boolean shareLocation;

    public Member toMember(PasswordEncoder passwordEncoder)
    {
        return Member.builder()
                .uuid(UUID.randomUUID())
                .phoneNumber(phoneNumber)
                .password((passwordEncoder.encode(password)))
                .authority(Authority.ROLE_USER)
                .shareLocation(false)
                .build();
    }


}
