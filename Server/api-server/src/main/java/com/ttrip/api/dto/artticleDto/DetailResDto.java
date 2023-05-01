package com.ttrip.api.dto.artticleDto;

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
@ApiModel(value = "게시글 상세 조화결과", description = "게시글 정보와 모집여부, 내작성글인지 정보가 있다")
public class DetailResDto {
        @ApiModelProperty(value = "게시글 아이디", example = "3")
        private Integer articleId;
        @ApiModelProperty(value = "게시글 작성자", example = "김싸피")
        private String authorName;
        @ApiModelProperty(value = "게시글 제목", example = "방콕 갈사람")
        private String title;
        @ApiModelProperty(value = "게시글 내용", example = "제곧내")
        private String content;
        @ApiModelProperty(value = "국가", example = "태국")
        private String nation;
        @ApiModelProperty(value = "도시", example = "방콕")
        private String city;
        @ApiModelProperty(value = "여행 시작 예정일", example = "2023-04-27T14:14:25.500")
        private LocalDateTime startDate;
        @ApiModelProperty(value = "여행 막마 예정일", example = "2023-04-27T14:14:25.500")
        private LocalDateTime endDate;
        @ApiModelProperty(value = "글 등록시간", example = "2023-04-27T14:14:25.500")
        private LocalDateTime createdAt;
        @ApiModelProperty(value = "T: 모집중 F:모집 마감", example = "T")
        private char status;
        @ApiModelProperty(value = "내 작성글이면 true인 boolean", example = "true")
        private Boolean isMine;

}
