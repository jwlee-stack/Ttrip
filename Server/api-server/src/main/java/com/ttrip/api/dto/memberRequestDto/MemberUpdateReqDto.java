package com.ttrip.api.dto.memberRequestDto;

import com.ttrip.core.customEnum.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
public class MemberUpdateReqDto {
    private MultipartFile file;
    private String nickname;
    private String intro;
    private String fcmToken;
    private Gender gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

}
