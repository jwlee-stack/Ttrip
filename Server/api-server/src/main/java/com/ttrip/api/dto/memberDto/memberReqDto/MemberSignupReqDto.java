package com.ttrip.api.dto.memberDto.memberReqDto;

import com.ttrip.core.entity.member.Member;
import com.ttrip.core.customEnum.Authority;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "회원가입 요청")
public class MemberSignupReqDto {
    @ApiModelProperty(value = "전화번호", example = "01012345678")
    private String phoneNumber;
    @ApiModelProperty(value = "비밀번호", example = "www1234")
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
