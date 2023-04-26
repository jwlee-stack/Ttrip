package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberLoginReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberSignupReqDto;
import com.ttrip.api.dto.tokenDto.tokenReqDto.TokenReqDto;
import com.ttrip.api.service.impl.MemberDetails;

public interface MemberService {
    DataResDto<?> findMemberById(Integer id);
    DataResDto<?> signup(MemberSignupReqDto memberSignupReqDto);
    DataResDto<?> login(MemberLoginReqDto memberLoginReqDto);
    DataResDto<?> logout(MemberDetails memberDetails);
    DataResDto<?> reissue(TokenReqDto tokenReqDto);
}
