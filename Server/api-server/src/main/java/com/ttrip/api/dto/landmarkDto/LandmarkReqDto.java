package com.ttrip.api.dto.landmarkDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@ToString
@ApiModel(value = "랜드마크 추가 요청")
public class LandmarkReqDto {
    @ApiModelProperty(value = "뱃지 사진", notes="파일 형식은 png 또는 jpg", example = "aa.png", allowEmptyValue = true)
    private MultipartFile badgeImgPath;
    @ApiModelProperty(value = "랜드마크 이름")
    private String landmarkName;
    @ApiModelProperty(value = "랜드마크 위도")
    private Double latitude;
    @ApiModelProperty(value = "랜드마크 경도")
    private Double longitude;
}