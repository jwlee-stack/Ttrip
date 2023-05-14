package com.ttrip.api.dto.artticleDto;

import com.ttrip.core.entity.article.Article;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.utils.EuclideanDistanceUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendResDto {
    @ApiModelProperty(value = "게시글 아이디", example = "3")
    private Integer articleId;
    @ApiModelProperty(value = "게시글 작성자", example = "김싸피")
    private String authorName;
    @ApiModelProperty(value = "게시글 작성자 프로필사진", example = "...jpg")
    private String imgPath;
    @ApiModelProperty(value = "게시글 제목", example = "방콕 갈사람")
    private String title;
    @ApiModelProperty(value = "게시글 내용", example = "제곧내")
    private String content;
    @ApiModelProperty(value = "국가", example = "태국")
    private String nation;
    @ApiModelProperty(value = "도시", example = "방콕")
    private String city;
    @ApiModelProperty(value = "여행 시작 예정일", example = "2023-04-27T14:14:25.500")
    private LocalDate startDate;
    @ApiModelProperty(value = "여행 마감 예정일", example = "2023-04-27T14:14:25.500")
    private LocalDate endDate;
    @ApiModelProperty(value = "글 등록시간", example = "2023-04-27T14:14:25.500")
    private LocalDateTime createdAt;
    @ApiModelProperty(value = "남은 닐짜", example = "2")
    private long dueDay;
    @ApiModelProperty(value = "T: 모집중", example = "T")
    private char status;
    @ApiModelProperty(value = "매칭 유사도", example = "78.5")
    private float similarity;

    @Builder
    public RecommendResDto(Member requester, Article article, EuclideanDistanceUtil similarity) {
        Member author = article.getMember();
        this.articleId = article.getArticleId();
        this.authorName = author.getNickname();
        this.imgPath = author.getProfileImgPath();
        this.title = article.getTitle();
        this.content = article.getTitle();
        this.nation = article.getNation();
        this.city = article.getCity();
        this.startDate = article.getStartDate();
        this.endDate = article.getEndDate();
        this.dueDay = ChronoUnit.DAYS.between(LocalDate.now(), article.getStartDate());
        this.createdAt = article.getCreatedAt();
        this.status = article.getStatus();
        this.similarity = similarity.getMatchingRate(requester.getSurvey(), author.getSurvey());
    }
}
