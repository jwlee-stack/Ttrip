package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberLoginReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberReportReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberSignupReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberUpdateReqDto;
import com.ttrip.api.dto.surveyDto.surverReqDto.SurveyReqDto;
import com.ttrip.api.dto.tokenDto.tokenReqDto.TokenReqDto;
import com.ttrip.api.service.MemberService;
import com.ttrip.api.service.impl.MemberDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(tags = "유저 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "회원가입 성공"),
            @ApiResponse(code = 400, message = "회원가입 실패")
    })
    @ApiOperation(value = "회원가입 API")
    @PostMapping
    public DataResDto<?> signup(@RequestBody MemberSignupReqDto memberSignupReqDto) {
        return memberService.signup(memberSignupReqDto);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "로그인 성공"),
            @ApiResponse(code = 400, message = "로그인 실패")
    })
    @ApiOperation(value = "로그인 API")
    @PostMapping("/login")
    public DataResDto<?> login(@RequestBody MemberLoginReqDto memberLoginReqDto) {
        return memberService.login(memberLoginReqDto);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "로그아웃 성공"),
            @ApiResponse(code = 400, message = "로그아웃 실패")
    })
    @ApiOperation(value = "로그아웃 API")
    @DeleteMapping("/logout")
    public DataResDto<?> logout(@AuthenticationPrincipal MemberDetails memberDetails) {
        return memberService.logout(memberDetails);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "닉네임 중복 체크 성공"),
            @ApiResponse(code = 400, message = "닉네임 중복 체크 실패")
    })
    @ApiOperation(value = "닉네임 중복 체크 API")
    @GetMapping("/{nickname}/exists")
    public DataResDto<?> checkNickname(@PathVariable String nickname) {
        return memberService.checkNickname(nickname);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 정보 세팅 성공"),
            @ApiResponse(code = 400, message = "회원 정보 세팅 실패")
    })
    @ApiOperation(value = "회원 정보 세팅 API")
    @PatchMapping("/setInfo")
    public DataResDto<?> setInfo(@ModelAttribute MemberUpdateReqDto memberUpdateReqDto,
                                      @AuthenticationPrincipal MemberDetails memberDetails) throws IOException {
        return memberService.setInfo(memberUpdateReqDto, memberDetails);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "accessToken 재발급 성공"),
            @ApiResponse(code = 400, message = "accessToken 재발급 실패")
    })
    @ApiOperation(value = "accessToken 재발급 API")
    @PatchMapping("/reissue")
    public DataResDto<?> reissue(@RequestBody TokenReqDto tokenReqDto,
                                 @AuthenticationPrincipal MemberDetails memberDetails) {
        return memberService.reissue(tokenReqDto, memberDetails.getMember().getMemberUuid());
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "여행 취향 저장 성공"),
            @ApiResponse(code = 400, message = "여행 취향 저장 실패")
    })
    @ApiOperation(value = "여행 취향 저장  API")
    @PatchMapping("/preferences")
    public DataResDto<?> updateSurvey(@RequestBody SurveyReqDto surveyReqDto,
                                      @AuthenticationPrincipal MemberDetails memberDetails) {
        return memberService.updateSurvey(surveyReqDto, memberDetails);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "프로필 조회 성공"),
            @ApiResponse(code = 400, message = "프로필 조회 실패")
    })
    @ApiOperation(value = "프로필 조회 API")
    @GetMapping("/{nickname}")
    public DataResDto<?> viewMemberInfo(@PathVariable String nickname) {
        return memberService.viewMemberInfo(nickname);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "신고 성공"),
            @ApiResponse(code = 400, message = "신고 실패")
    })
    @ApiOperation(value = "신고")
    @PostMapping("/report")
    public DataResDto<?> reportMember(@RequestBody MemberReportReqDto memberReportReqDto,
                                      @AuthenticationPrincipal MemberDetails memberDetails) {
        return memberService.reportMember(memberReportReqDto, memberDetails);
    }
}