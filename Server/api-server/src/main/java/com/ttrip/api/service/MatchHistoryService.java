package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.MatchMakerReqDto;
import com.ttrip.core.entity.member.Member;

import java.util.UUID;

public interface MatchHistoryService {
    DataResDto<?> historyMaker(MatchMakerReqDto matchMakerReqDto, Member member);
    DataResDto<?> historySearch(Member member);
}
