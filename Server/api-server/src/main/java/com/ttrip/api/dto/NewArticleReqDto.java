package com.ttrip.api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NewArticleReqDto {
    private UUID uuid;
    private String title;
    private String content;
    private String nation;
    private String city;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
