package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.MatchMakerReqDto;
import com.ttrip.api.service.MatchHistoryService;
import com.ttrip.api.service.impl.MemberDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "매칭 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matchHistory")
public class MatchHistoryController {

    private final MatchHistoryService matchHistoryService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "매칭을 조회했습니다."),
            @ApiResponse(code = 400, message = "매칭 조회 실패")
    })
    @ApiOperation(value = "매칭 조회 API", httpMethod = "GET")
    @GetMapping
    public DataResDto<?> historySearch(@AuthenticationPrincipal MemberDetails memberDetails) {
        return matchHistoryService.historySearch(memberDetails.getMember());
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "매칭을 조회했습니다."),
            @ApiResponse(code = 400, message = "매칭 조회 실패")
    })
    @ApiOperation(value = "매칭 기록 생성 API", httpMethod = "GET")
    @PostMapping
    public DataResDto<?> historyMaker(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody MatchMakerReqDto matchMakerReqDto) {
        return matchHistoryService.historyMaker(matchMakerReqDto, memberDetails.getMember());
    }
}
