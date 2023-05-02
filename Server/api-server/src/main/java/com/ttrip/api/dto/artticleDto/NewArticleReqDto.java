package com.ttrip.api.dto.artticleDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel(value = "NewArticleReqDto: 게시글 생성에 필요한 정보", description = "게시글 타이틀 내용 나라 도시 여행 시작 마감 시간")
public class NewArticleReqDto {
    @ApiModelProperty(value = "게시글 타이틀", example = "5시15분인데 집갈사람")
    private String title;
    @ApiModelProperty(value = "게시글 내용", example = "집에가고 싶어요:(")
    private String content;
    @ApiModelProperty(value = "나라", example = "대한민국")
    private String nation;
    @ApiModelProperty(value = "도시", example = "구미시")
    private String city;
    @ApiModelProperty(value = "여행 시작예정시간", example = "2023-04-27")
    private LocalDate startDate;
    @ApiModelProperty(value = "여행 마감 예정 시간", example = "2023-04-27")
    private LocalDate endDate;
}
