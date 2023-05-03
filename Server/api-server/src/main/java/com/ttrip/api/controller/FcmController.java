package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.fcmMessageDto.FcmMessageReqDto;
import com.ttrip.api.service.FcmService;
import com.ttrip.api.service.impl.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/fcm")
@RequiredArgsConstructor
public class FcmController {
    private final FcmService fcmService;

    @PostMapping
    public DataResDto<?> sendMessage(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody FcmMessageReqDto fcmMessageReqDto) throws IOException {
        return fcmService.sendMessageTo(memberDetails.getMember(), fcmMessageReqDto) ;
    }
}
