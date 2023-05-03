package com.ttrip.api.dto.fcmMessageDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@ApiModel(value = "FcmMessageReqDto", description = "fcm 요청 DTO")
public class FcmMessageReqDto {
    @ApiModelProperty(value = "요청 타입", notes = "0: Live매칭 요청  1: 매칭요청 결과 2:article 3: Chat 4:매칭 평가 type으로 나눠서 사용")
    private int type;
    @ApiModelProperty(value = "타켓 유저의 UUID")
    private UUID targetUuid;
    @ApiModelProperty(value = "추가 정보의 id", notes = "게시글이나 채팅룸 용 정보 받을뗴")
    private String extraId;
    @ApiModelProperty(value = "추가정보", notes = "채팅 메세지이나 매칭 수락 여부 확인 전달용")
    private String extraData;
}
