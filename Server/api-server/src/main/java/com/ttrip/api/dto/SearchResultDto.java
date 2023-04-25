package com.ttrip.api.dto;

import com.ttrip.core.entity.article.Article;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchResultDto {
    private Integer articleId;
    private String authorName;
    private String title;
    private String content;
    private String nation;
    private String city;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdDate;
    private char status;


}
