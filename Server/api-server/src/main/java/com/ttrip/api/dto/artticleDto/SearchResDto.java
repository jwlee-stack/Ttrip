package com.ttrip.api.dto.artticleDto;

import com.ttrip.core.entity.article.Article;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
    @ApiModelProperty(value = "나라", example = "태국")
    private String nation;
    @ApiModelProperty(value = "도시", example = "방콕")
    private String city;
    @ApiModelProperty(value = "여행 시작 닐짜", example = "2023-04-27")
    private LocalDate startDate;
    @ApiModelProperty(value = "여행 마감 닐짜", example = "2023-04-27")
    private LocalDate endDate;
    @ApiModelProperty(value = "남은 닐짜", example = "2")
    private long dueDay;
    @ApiModelProperty(value = "글 작성 시간", example = "2023-04-27T14:14:25.500")
    private LocalDateTime createdAt;
    @ApiModelProperty(value = "디폴트:T, 마감시: F ", example = "T")
    private char status;

    public static SearchResDto toBuild(Article article){
        return SearchResDto.builder()
                .articleId(article.getArticleId())
                .authorName(article.getMember().getNickname())
                .title(article.getTitle())
                .dueDay(ChronoUnit.DAYS.between(LocalDate.now(), article.getStartDate()))
                .nation(article.getNation())
                .city(article.getCity())
                .status(article.getStatus())
                .createdAt(article.getCreatedAt())
                .startDate(article.getStartDate())
                .endDate(article.getEndDate())
                .build();
    }
}
