package com.ttrip.api.dto.chatroomDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Builder
@Data
@ApiModel(value = "ChatMessageResDto", description = "채팅방 내용조회")
public class ChatMessageResDto {
    @ApiModelProperty(value = "내메세지인지 확인boolean")
    private Boolean isMine;
    @ApiModelProperty(value = "챗메세지 내용")
    private String content;
    @ApiModelProperty(value = "발송시간")
    private LocalDateTime createdAt;
}
