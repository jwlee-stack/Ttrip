package com.ttrip.api.dto.artticleDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@ApiModel(value = "신청글 조회", description = "신청글 정보와 신청자 정보 제공")
public class SearchApplyResDto {
    @ApiModelProperty(value = "신청글 id", example = "1")
    private Integer applyId;
    @ApiModelProperty(value = "신청자 uuid", example = "123e4567-e89b-12d3-a456-426655440000")
    private UUID applicantUuid;
    @ApiModelProperty(value = "신청자 닉네임", example = "KimTtrip")
    private String applicantNickname;
    @ApiModelProperty(value = "신청글 내용", example = "집에가고 싶어요")
    private String requestContent;
    @ApiModelProperty(value = "신청자 프로필이미지주소", example = "")
    private String imgPath;
    @ApiModelProperty(value = "매칭 유사도", example = "78.5")
    private float similarity;
}
