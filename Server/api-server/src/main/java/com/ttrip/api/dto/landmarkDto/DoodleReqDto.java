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
@ApiModel(value = "낙서 저장 요청")
public class DoodleReqDto {
    @ApiModelProperty(value = "낙서 위도")
    private Double latitude;
    @ApiModelProperty(value = "낙서 경도")
    private Double longitude;
    @ApiModelProperty(value = "낙서 x좌표")
    private Double positionX;
    @ApiModelProperty(value = "낙서 y좌표")
    private Double positionY;
    @ApiModelProperty(value = "낙서 z좌표")
    private Double positionZ;
    @ApiModelProperty(value = "낙서 사진", notes="파일 형식은 png 또는 jpg", example = "aa.png", allowEmptyValue = true)
    private MultipartFile doodleImgPath;
    @ApiModelProperty(value = "랜드마크 Id")
    private Integer landmarkId;
}