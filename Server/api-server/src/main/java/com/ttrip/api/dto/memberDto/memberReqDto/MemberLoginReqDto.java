package com.ttrip.api.dto.memberDto.memberReqDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginReqDto {
    private String phoneNumber;
    private String password;

    // Login ID/PW 를 기반으로 AuthenticationToken 생성
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(phoneNumber, password);
    }
}
