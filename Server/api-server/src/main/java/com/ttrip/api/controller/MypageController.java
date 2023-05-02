package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.mypageDto.mypageReqDto.BackgroundUpdateReqDto;
import com.ttrip.api.dto.mypageDto.mypageReqDto.InfoUpdateReqDto;
import com.ttrip.api.dto.mypageDto.mypageReqDto.ProfileUpdateReqDto;
import com.ttrip.api.dto.surveyDto.surverReqDto.SurveyReqDto;
import com.ttrip.api.service.MemberService;
import com.ttrip.api.service.MypageService;
import com.ttrip.api.service.impl.MemberDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "마이페이지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {
    private final MypageService mypageService;
    private final MemberService memberService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "내 게시글 조회 성공"),
            @ApiResponse(code = 400, message = "내 게시글 조회 실패")
    })
    @ApiOperation(value = "내 게시글 조회")
    @GetMapping("/view/articles")
    public DataResDto<?> viewMyArticles(@AuthenticationPrincipal MemberDetails memberDetails) {
        return mypageService.viewMyArticles(memberDetails);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 정보 변경 성공"),
            @ApiResponse(code = 400, message = "회원 정보 변경 실패")
    })
    @ApiOperation(value = "회원 정보 변경")
    @PatchMapping("/update")
    public DataResDto<?> updateMember(@RequestBody InfoUpdateReqDto infoUpdateReqDto, @AuthenticationPrincipal MemberDetails memberDetails) {
        return mypageService.updateMember(infoUpdateReqDto, memberDetails);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "프로필 사진 변경 성공"),
            @ApiResponse(code = 400, message = "프로필 사진 변경 실패")
    })
    @ApiOperation(value = "프로필 사진 변경")
    @PatchMapping("/update/profile")
    public DataResDto<?> updateProfileImg(@ModelAttribute ProfileUpdateReqDto profileUpdateReqDto, @AuthenticationPrincipal MemberDetails memberDetails) {
        return mypageService.updateProfileImg(profileUpdateReqDto, memberDetails);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "배경 사진 변경 성공"),
            @ApiResponse(code = 400, message = "배경 사진 변경 실패")
    })
    @ApiOperation(value = "배경 사진 변경")
    @PatchMapping("/update/background")
    public DataResDto<?> updateBackgroundImg(@ModelAttribute BackgroundUpdateReqDto backGroundUpdateReqDto, @AuthenticationPrincipal MemberDetails memberDetails) {
        return mypageService.updateBackgroundImg(backGroundUpdateReqDto, memberDetails);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "여행 취향 테스트 성공"),
            @ApiResponse(code = 400, message = "여행 취향 테스트 실패")
    })
    @ApiOperation(value = "여행 취향 테스트 변경")
    @PatchMapping("/update/preferences")
    public DataResDto<?> updateProfileImg(@RequestBody SurveyReqDto surveyReqDto, @AuthenticationPrincipal MemberDetails memberDetails) {
        return memberService.updateSurvey(surveyReqDto, memberDetails);
    }
}
