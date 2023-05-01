package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberLoginReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberReportReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberSignupReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberUpdateReqDto;
import com.ttrip.api.dto.surveyDto.surverReqDto.SurveyReqDto;
import com.ttrip.api.dto.tokenDto.tokenReqDto.TokenReqDto;
import com.ttrip.api.service.impl.MemberDetails;

import java.io.IOException;
import java.util.UUID;

public interface MemberService {
    DataResDto<?> signup(MemberSignupReqDto memberSignupReqDto);
    DataResDto<?> login(MemberLoginReqDto memberLoginReqDto);
    DataResDto<?> logout(MemberDetails memberDetails);
    DataResDto<?> reissue(TokenReqDto tokenReqDto, UUID uuid);
    DataResDto<?> updateMember (MemberUpdateReqDto memberUpdateReqDto, MemberDetails memberDetails) throws IOException;
    DataResDto<?> checkNickname(String nickname);
    DataResDto<?> updateSurvey(SurveyReqDto surveyReqDto, MemberDetails memberDetails);
    DataResDto<?> viewMemberInfo(String nickname);
    DataResDto<?> reportMember(MemberReportReqDto memberReportReqDto, MemberDetails memberDetails);
}
