package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/")
    public DataResDto<?> getMemberExample() {

        return memberService.findMemberById(1);
    }
}