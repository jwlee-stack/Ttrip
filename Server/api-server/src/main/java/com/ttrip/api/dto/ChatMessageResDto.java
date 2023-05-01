package com.ttrip.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Builder
@Data
public class ChatMessageResDto {
    private Boolean isMine;
    private String content;
    private LocalDateTime createdAt;
}
