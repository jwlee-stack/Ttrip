package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.MatchRateReqDto;
import com.ttrip.core.entity.member.Member;

import java.util.UUID;

public interface MatchHistoryService {
    DataResDto<?> historyMaker(MatchRateReqDto matchRateReqDto, Member member);
    DataResDto<?> historySearch(Member member);
}
