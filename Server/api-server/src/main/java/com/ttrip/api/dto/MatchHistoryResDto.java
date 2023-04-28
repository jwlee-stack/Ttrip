package com.ttrip.api.dto;

import com.ttrip.core.entity.matchHistory.MatchHistory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchHistoryResDto {
    private String evaluatedNickname;
    private Integer rate;
}
