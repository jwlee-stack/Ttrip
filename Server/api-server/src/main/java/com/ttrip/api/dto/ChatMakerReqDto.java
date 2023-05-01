package com.ttrip.api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ChatMakerReqDto {
    private UUID opponentUserUuid;
    private Integer ArticleId;
}
