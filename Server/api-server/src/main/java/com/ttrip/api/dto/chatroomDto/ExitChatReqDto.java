package com.ttrip.api.dto.chatroomDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ExitChatReqDto", description = "채팅 나가기위한 dto")
public class ExitChatReqDto {
    @ApiModelProperty(value = "채팅방 id")
    private Integer chatroomId;
}
