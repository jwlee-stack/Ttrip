package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.service.LandmarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "랜드마크 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/landmarks")
public class LandmarkController {
    private final LandmarkService landmarkService;

    @ApiResponses({@ApiResponse(code = 200, message = "랜드마크 목록 조회 성공 시 응답")})
    @ApiOperation(value = "랜드마크 목록 조회 API")
    @GetMapping
    public DataResDto<?> getLandmarkList() {
        return landmarkService.getLandmarkList();
    }
}