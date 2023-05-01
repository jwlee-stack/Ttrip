package com.ttrip.api.dto.artticleDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SearchReqDto: 검색옵션", description = "어떤 조회인지, 나라, 도시, 키워드를 입력 받는다.")
public class SearchReqDto {
    @ApiModelProperty(value = "0: 전체 조회, 1: 나라 도시 조회, 2: 제목 내용에서 키웟드 조회", example = "1")
    private Integer condition;
    @ApiModelProperty(value = "나라", example = "미국")
    private String nation;
    @ApiModelProperty(value = "도시", example = "알라바마")
    private String city;
    @ApiModelProperty(value = "검색할 쿼리", example = "sweet home alabama")
    private String keyword;
}
