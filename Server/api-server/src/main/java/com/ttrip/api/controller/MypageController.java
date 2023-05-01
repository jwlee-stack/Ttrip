package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.service.MypageService;
import com.ttrip.api.service.impl.MemberDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "마이페이지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {
    private final MypageService mypageService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "내 게시글 조회 성공"),
            @ApiResponse(code = 400, message = "내 게시글 조회 실패")
    })
    @ApiOperation(value = "내 게시글 조회")
    @GetMapping("/view/articles")
    public DataResDto<?> viewMyArticles(@AuthenticationPrincipal MemberDetails memberDetails)
    {
        return mypageService.viewMyArticles(memberDetails);
    }
}
