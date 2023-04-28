package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberLoginReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberSignupReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberUpdateReqDto;
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
    public DataResDto<?> signup(@RequestBody MemberSignupReqDto memberSignupReqDto)
    {
        return memberService.signup(memberSignupReqDto);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "로그인 성공"),
            @ApiResponse(code = 400, message = "로그인 실패")
    })
    @ApiOperation(value = "로그인 API")
    @PostMapping("/login")
    public DataResDto<?> login(@RequestBody MemberLoginReqDto memberLoginReqDto)
    {
        return memberService.login(memberLoginReqDto);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "로그아웃 성공"),
            @ApiResponse(code = 400, message = "로그아웃 실패")
    })
    @ApiOperation(value = "로그아웃 API")
    @GetMapping("/logout")
    public DataResDto<?> logout(@AuthenticationPrincipal MemberDetails memberDetails)
    {
        return memberService.logout(memberDetails);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "닉네임 중복 체크 성공"),
            @ApiResponse(code = 400, message = "닉네임 중복 체크 실패")
    })
    @ApiOperation(value = "닉네임 중복 체크 API")
    @GetMapping("/{nickname}/exists")
    public DataResDto<?> checkNickname(@PathVariable String nickname)
    {
        return memberService.checkNickname(nickname);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 정보 업데이트 성공"),
            @ApiResponse(code = 400, message = "회원 정보 업데이트 실패")
    })
    @ApiOperation(value = "회원 정보 업데이트 API")
    @PatchMapping("/update")
    public DataResDto<?> updateMember(@ModelAttribute MemberUpdateReqDto memberUpdateReqDto,
                                      @AuthenticationPrincipal MemberDetails memberDetails) throws IOException
    {
        return memberService.updateMember(memberUpdateReqDto,memberDetails);
    }
}