package com.ttrip.api.service.impl;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.MatchHistoryResDto;
import com.ttrip.api.dto.MatchRateReqDto;
import com.ttrip.api.exception.UnauthorizationException;
import com.ttrip.api.service.MatchHistoryService;
import com.ttrip.core.entity.matchHistory.MatchHistory;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.matchHistory.MatchHistoryRepository;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MatchHistoryServiceImpl implements MatchHistoryService {
    private final MatchHistoryRepository matchHistoryRepository;
    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public DataResDto<?> historyMaker(MatchRateReqDto matchRateReqDto, Member member) {
        MatchHistory matchHistory = matchHistoryRepository.findByMatchHistoryId((matchRateReqDto.getMatchHistoryId()))
                .orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.MATCH_NOT_EXIST.getMessage()));
        //다른사람의 평가인경우 차단
        if(! matchHistory.getEvaluator().equals(member)){
            throw new UnauthorizationException(ErrorMessageEnum.NO_AUTH.getMessage());
        }
        //아직 평가 안했다면
        if (matchHistory.getRate() == null){
            matchHistory.setRate(matchRateReqDto.getRate());
            matchHistoryRepository.save(matchHistory);
            return DataResDto.builder().message("매칭을 평가했습니다.").data(true).build();
        }else {
            //이미 평가했다면 차단
            return DataResDto.builder().message("이미 평가한 매칭입니다.").data(false).build();
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
                        .matchHistoryId(matchHistory.getMatchHistoryId())
                        .build();
                matchHistoryResDtoList.add(matchHistoryResDto);
            }
        }
        return DataResDto.builder().message("매칭을 조회했습니다.").data(matchHistoryResDtoList).build();
    }
}