package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberLoginReqDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberSignupReqDto;
import com.ttrip.api.dto.memberRequestDto.MemberUpdateReqDto;
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
            @ApiResponse(code = 200, message = "멤버 프로필 이미지 조회가 성공한 경우"),
            @ApiResponse(code = 400, message = "존재하지 않는 회원일 경우")
    })
    @ApiOperation(value = "유저 프로필 이미지 조회 API")

    @PostMapping
    public DataResDto<?> signup(@RequestBody MemberSignupReqDto memberSignupReqDto)
    {
        return memberService.signup(memberSignupReqDto);
    }

    @PostMapping("/login")
    public DataResDto<?> login(@RequestBody MemberLoginReqDto memberLoginReqDto)
    {
        return memberService.login(memberLoginReqDto);
    }

    @GetMapping("/logout")
    public DataResDto<?> logout(@AuthenticationPrincipal MemberDetails memberDetails)
    {
        return memberService.logout(memberDetails);
    }

    @GetMapping("/{nickname}/exists")
    public DataResDto<?> checkNickname(@PathVariable String nickname)
    {
        return memberService.checkNickname(nickname);
    }

    @PatchMapping("/update")
    public DataResDto<?> updateMember(@ModelAttribute MemberUpdateReqDto memberUpdateReqDto,
                                      @AuthenticationPrincipal MemberDetails memberDetails) throws IOException
    {
        return memberService.updateMember(memberUpdateReqDto,memberDetails);
    }
}