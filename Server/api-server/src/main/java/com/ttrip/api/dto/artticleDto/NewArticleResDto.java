package com.ttrip.api.dto.artticleDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "새로생성된 게시글 아이디", description = "새로 생성된 게시글 아이디")
public class NewArticleResDto {
    @ApiModelProperty(value = "새로생성된 게시글id", example = "153")
    private Integer articleId;

}
