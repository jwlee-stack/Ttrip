package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.openViduDto.SessionJoinReqDto;
import com.ttrip.api.service.OpenViduService;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class OpenviduController {
    private static final Logger logger = LogManager.getLogger(OpenviduController.class);
    private final OpenViduService openViduService;

    @PostMapping
    public DataResDto<?> createSession()
            throws OpenViduJavaClientException, OpenViduHttpException {
        return openViduService.createSession();
    }

    @PostMapping("/{sessionId}/connections")
    public DataResDto<?> joinSession(@RequestBody(required = false) SessionJoinReqDto sessionJoinReqDto)
            throws OpenViduJavaClientException, OpenViduHttpException {
        return openViduService.joinSession(sessionJoinReqDto);
    }
}