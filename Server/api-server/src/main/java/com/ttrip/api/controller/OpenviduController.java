package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.openViduDto.SessionJoinReqDto;
import com.ttrip.api.service.OpenViduService;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Openvidu 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class OpenviduController {
    private static final Logger logger = LogManager.getLogger(OpenviduController.class);
    private final OpenViduService openViduService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "세션 생성 성공 시 응답"),
            @ApiResponse(code = 401, message = "인증 실패")
    })
    @ApiOperation(value = "세션 생성 API")
    @PostMapping
    public DataResDto<?> createSession()
            throws OpenViduJavaClientException, OpenViduHttpException {
        return openViduService.createSession();
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "토큰 발급 성공 시 응답"),
            @ApiResponse(code = 401, message = "인증 실패")
    })
    @ApiOperation(value = "토큰 발급 API")
    @PostMapping("/{sessionId}/connections")
    public DataResDto<?> joinSession(@RequestBody(required = false) SessionJoinReqDto sessionJoinReqDto)
            throws OpenViduJavaClientException, OpenViduHttpException {
        return openViduService.joinSession(sessionJoinReqDto);
    }
}