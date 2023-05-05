package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.fcmMessageDto.FcmMessageReqDto;
import com.ttrip.api.service.FcmService;
import com.ttrip.api.service.impl.MemberDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
@Api(tags = "FCM관련 API")
@RestController
@RequestMapping(value = "/api/fcm")
@RequiredArgsConstructor
public class FcmController {
    private final FcmService fcmService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "FCM 메세지 발송성공했습니다."),
            @ApiResponse(code = 400, message = "FCM 메세지 발송 실패했습니다.")
    })
    @ApiOperation(value = "FCM 발송 API", httpMethod = "POST")
    @PostMapping
    public DataResDto<?> sendMessage(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody FcmMessageReqDto fcmMessageReqDto) throws IOException {
        return fcmService.sendMessageTo(memberDetails.getMember(), fcmMessageReqDto) ;
    }

    @Scheduled(cron = "0 0 16 * * ?")
    @ApiOperation(value = "rate", notes = "매칭 평가 여부 확인 후 fcm 발송", httpMethod = "GET")
    @GetMapping("/rate")
    public void rate (@AuthenticationPrincipal MemberDetails memberDetails) throws IOException{
        fcmService.rate();
    }
    @Scheduled(cron = "0 0 * * * ?")
    @ApiOperation(value = "liveRate", notes = "라이브 매칭 평가 여부 확인 후 fcm 발송", httpMethod = "GET")
    @GetMapping("/liveRate")
    public void liveRate (@AuthenticationPrincipal MemberDetails memberDetails) throws IOException{
        fcmService.liveRate();
    }
}
