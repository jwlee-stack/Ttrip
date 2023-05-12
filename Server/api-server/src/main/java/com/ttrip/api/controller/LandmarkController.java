package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.landmarkDto.LandmarkReqDto;
import com.ttrip.api.dto.landmarkDto.ReceiveBadgeReqDto;
import com.ttrip.api.service.LandmarkService;
import com.ttrip.api.service.impl.MemberDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "랜드마크 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/landmarks")
public class LandmarkController {
    private final LandmarkService landmarkService;

    @ApiResponses({@ApiResponse(code = 200, message = "랜드마크 추가 성공 시 응답")})
    @ApiOperation(value = "랜드마크 추가 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DataResDto<?> addLandmark(@ModelAttribute LandmarkReqDto landmarkReqDto) {
        return landmarkService.addLandmark(landmarkReqDto);
    }
    @ApiResponses({@ApiResponse(code = 200, message = "랜드마크 목록 조회 성공 시 응답")})
    @ApiOperation(value = "랜드마크 목록 조회 API")
    @GetMapping
    public DataResDto<?> getLandmarkList() {
        return landmarkService.getLandmarkList();
    }

    @ApiResponses({@ApiResponse(code = 200, message = "뱃지 발급 성공 시 응답")})
    @ApiOperation(value = "뱃지 발급 API")
    @PostMapping("/badges")
    public DataResDto<?> receiveBadge(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody ReceiveBadgeReqDto receiveBadgeReqDto) {
        return landmarkService.receiveBadge(memberDetails.getMember(), receiveBadgeReqDto);
    }

    @ApiResponses({@ApiResponse(code = 200, message = "뱃지 목록 조회 성공 시 응답")})
    @ApiOperation(value = "뱃지 조회 API")
    @GetMapping("/badges")
    public DataResDto<?> getBadgeList(@AuthenticationPrincipal MemberDetails memberDetails) {
        return landmarkService.getBadgeList(memberDetails.getMember());
    }
}