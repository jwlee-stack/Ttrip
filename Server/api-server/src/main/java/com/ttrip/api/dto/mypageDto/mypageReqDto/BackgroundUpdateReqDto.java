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
@ApiModel(value = "배경 사진 업데이트 요청")
public class BackgroundUpdateReqDto {
    @ApiModelProperty(value = "변경할 배경 사진", notes="파일 형식은 png 또는 jpg", example = "aa.png",allowEmptyValue = true)
    private MultipartFile backgroundImg;
}
