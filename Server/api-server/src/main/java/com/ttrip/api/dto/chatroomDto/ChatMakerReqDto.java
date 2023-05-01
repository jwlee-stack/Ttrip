package com.ttrip.api.dto.chatroomDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.UUID;

@Data
@ApiModel(value = "chatMakerReqDto", description = "채팅방 생성을 위한 dto")
public class ChatMakerReqDto {
    @ApiModelProperty(value = "상대방 uuid")
    private UUID opponentUserUuid;
    @ApiModelProperty(value = "게시글 아이디")
    private Integer ArticleId;
}
