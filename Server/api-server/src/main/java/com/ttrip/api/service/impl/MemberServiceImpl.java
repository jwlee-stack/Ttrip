package com.ttrip.api.service.impl;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.service.MemberService;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public DataResDto<?> findMemberById(Integer id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.USER_NOT_EXIST.getMessage()));
        return DataResDto.builder().message("example").data(member).build();
    }
}
