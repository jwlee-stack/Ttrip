package com.ttrip.api.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
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
