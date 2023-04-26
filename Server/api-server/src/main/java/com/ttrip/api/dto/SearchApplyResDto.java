package com.ttrip.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class SearchApplyResDto {
    private Integer applyId;
    private UUID applicantUuid;
    private String applicantNickname;
    private String requestContent;
    private String imgPath;
    private float similarity;
}
