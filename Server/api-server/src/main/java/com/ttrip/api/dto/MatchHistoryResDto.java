package com.ttrip.api.dto;

import com.ttrip.core.entity.matchHistory.MatchHistory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
@ApiModel(value = "MatchHistoryResDto" ,description = "이전 매칭 기록")
public class MatchHistoryResDto {
    @ApiModelProperty(value = "피평가자닉네임", example = "김싸피")
    private String evaluatedNickname;
    @ApiModelProperty(value = "평가점수", example = "5")
    private Integer rate;
    @ApiModelProperty(value = "매칭이력 id", example = "3")
    private Integer matchHistoryId;
}
