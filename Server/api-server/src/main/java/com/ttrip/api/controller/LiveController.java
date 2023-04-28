package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.memberDto.memberReqDto.MemberSignupReqDto;
import com.ttrip.api.service.LiveService;
import com.ttrip.api.service.impl.MemberDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "LIVE 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/live")
public class LiveController {
    private final LiveService liveService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "도시별 유저 정보 목록 조회 성공 시 응답"),
    })
    @ApiOperation(value = "Live 도시에 존재하는 유저 정보 목록 조회 API")

    @GetMapping("/{city}/{lng}/{lat}")
    public DataResDto<?> getMemberInfoInCity(
            @PathVariable("city") String city,
            @PathVariable("lng") double lng,
            @PathVariable("lat") double lat)
    {
        return liveService.getMembersInCity(city, lng, lat);
    }
}
