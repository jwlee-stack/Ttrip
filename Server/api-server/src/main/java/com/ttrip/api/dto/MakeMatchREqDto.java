package com.ttrip.api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MakeMatchREqDto {
    private Integer articleId;
    private UUID opponentUuid;
}
