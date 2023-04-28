package com.ttrip.api.service.impl;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.MatchHistoryResDto;
import com.ttrip.api.dto.MatchMakerReqDto;
import com.ttrip.api.service.MatchHistoryService;
import com.ttrip.core.entity.matchHistory.MatchHistory;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.matchHistory.MatchHistoryRepository;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MatchHistoryServiceImpl implements MatchHistoryService {
    private final MatchHistoryRepository matchHistoryRepository;
    private final MemberRepository memberRepository;
    @Override
    public DataResDto<?> historyMaker(MatchMakerReqDto matchMakerReqDto, Member member) {
        if (memberRepository.existsByMemberUuid(matchMakerReqDto.getEvaluatedUuid())) {
            MatchHistory matchHistory = matchMakerReqDto.toMatchHistory(member, memberRepository.findByMemberUuid(matchMakerReqDto.getEvaluatedUuid()).get());
            matchHistoryRepository.save(matchHistory);
            return DataResDto.builder().message("매칭을 평가했습니다.").data(true).build();
        }else{
            throw new NoSuchElementException(ErrorMessageEnum.USER_NOT_EXIST.getMessage());
        }
    }

    @Override
    public DataResDto<?> historySearch(Member member) {
        List<MatchHistoryResDto> matchHistoryResDtoList = new ArrayList<>();
        for(MatchHistory matchHistory: matchHistoryRepository.findByEvaluator(member)){
            if(memberRepository.existsById(matchHistory.getEvaluated().getMemberId())){
                MatchHistoryResDto matchHistoryResDto = MatchHistoryResDto.builder()
                        .rate(matchHistory.getRate())
                        .evaluatedNickname(matchHistory.getEvaluated().getNickname())
                        .build();
                matchHistoryResDtoList.add(matchHistoryResDto);
            }
        }
        return DataResDto.builder().message("매칭을 조회했습니다.").data(matchHistoryResDtoList).build();
    }
}