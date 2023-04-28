package com.ttrip.api.dto;

import com.ttrip.core.entity.matchHistory.MatchHistory;
import com.ttrip.core.entity.member.Member;
import lombok.Data;

import java.util.UUID;

@Data
public class MatchMakerReqDto {
    private UUID evaluatedUuid;
    private Integer rate;

    public MatchHistory toMatchHistory(Member evaluator, Member evaluated)
    {
        return MatchHistory.builder()
                .evaluator(evaluator)
                .evaluated(evaluated)
                .rate(rate)
                .build();
    }
}
