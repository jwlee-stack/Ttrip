package com.ttrip.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ApplyReqDto: 요청글 작성", description = "대상게시글id, 요청글 내용을 가진다.")
public class ApplyReqDto {
    @ApiModelProperty(value = "게시글 아이디", example = "1")
    private Integer articleId;
    @ApiModelProperty(value = "요청글 내용", example = "같이가요")
    private String requestContent;
}
