package com.ttrip.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "SearchResDto", description = "검색 결과 게시글정보와 작성자 정보")
public class SearchResDto {
    @ApiModelProperty(value = "게시글 아이디", example = "1")
    private Integer articleId;
    @ApiModelProperty(value = "작성자 닉네임", example = "김트립")
    private String authorName;
    @ApiModelProperty(value = "게시글 제목", example = "방콕 여행가시분 있나요?")
    private String title;
    @ApiModelProperty(value = "게시글 내용", example = "안녕하세요 이번주말에 방콕여행을 가는데 같이...")
    private String content;
    @ApiModelProperty(value = "나라", example = "태국")
    private String nation;
    @ApiModelProperty(value = "도시", example = "방콕")
    private String city;
    @ApiModelProperty(value = "여행 시작 시간", example = "2023-04T27:14:14:25")
    private LocalDateTime startDate;
    @ApiModelProperty(value = "여행 마감 시간", example = "2023-04T27:14:14:25")
    private LocalDateTime endDate;
    @ApiModelProperty(value = "글 작성 시간", example = "2023-04T27:14:14:25")
    private LocalDateTime createdAt;
    @ApiModelProperty(value = "디폴트:T, 마감시: F ", example = "T")
    private char status;

}
