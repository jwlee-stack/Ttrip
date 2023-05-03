package com.ttrip.api.dto.matchDto;

import com.ttrip.core.entity.matchHistory.MatchHistory;
import com.ttrip.core.entity.member.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@ApiModel(value = "MatchRateReqDto: 매칭 기록 생성", description = "매칭 기록 생성")
public class MatchRateReqDto {
    @ApiModelProperty(value = "매칭 id", example = "854")
    private Integer matchHistoryId;
    @ApiModelProperty(value = "평가 점수", example = "4")
    private Integer rate;
}
