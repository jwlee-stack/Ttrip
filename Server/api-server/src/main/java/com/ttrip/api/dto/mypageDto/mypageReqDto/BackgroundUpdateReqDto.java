package com.ttrip.api.dto.mypageDto.mypageReqDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@ApiModel(value = "프로필 사진 업데이트 요청")
public class ProfileUpdateReqDto {
    @ApiModelProperty(value = "변경할 프로필 사진", notes="파일 형식은 png 또는 jpg", example = "aa.png")
    private MultipartFile profileImg;
    @ApiModelProperty(value = "변경할 마커 사진", notes="파일 형식은 png 또는 jpg", example = "bb.png")
    private MultipartFile markerImg;

}
