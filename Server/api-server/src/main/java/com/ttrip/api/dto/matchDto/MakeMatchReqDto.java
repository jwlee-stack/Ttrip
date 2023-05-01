package com.ttrip.api.dto.matchDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.UUID;

@Data
@ApiModel(value = "MakeMatchReqDto", description = "매칭 수락시 매칭여부하기위해 게시글아이디와 상대방Uuid정보를 필요로 합니다.")
public class MakeMatchReqDto {
    @ApiModelProperty(value = "게시글 아이디")
    private Integer articleId;
    @ApiModelProperty(value = "상대방 uuid")
    private UUID opponentUuid;
}
