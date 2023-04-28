package com.ttrip.api.dto.memberDto.memberReqDto;

import com.ttrip.core.customEnum.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
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
@ApiModel(value = "회원 정보 업데이트 요청")
public class MemberUpdateReqDto {
    @ApiModelProperty(value = "변경할 프로필 사진", notes="파일 형식은 png 또는 jpg", example = "aa.png")
    private MultipartFile profileImg;
    @ApiModelProperty(value = "변경할 마커 사진", notes="파일 형식은 png 또는 jpg", example = "bb.png")
    private MultipartFile markerImg;
    @ApiModelProperty(value = "변경할 닉네임", notes="6자 이내", example = "새닉넴")
    private String nickname;
    @ApiModelProperty(value = "변경할 소개말", notes="20자 이내", example = "할로할로")
    private String intro;
    @ApiModelProperty(value = "변경할 fcm 토큰", notes="푸시 알림을 위한 토큰", example = "158qwe456wre8we1r3...")
    private String fcmToken;
    @ApiModelProperty(value = "변경할 성별", notes="MALE/FEMALE", example = "MALE")
    private Gender gender;
    @ApiModelProperty(value = "변경할 생일", notes="String", example = "1999-06-06")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

}
