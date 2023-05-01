package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.openViduDto.SessionJoinReqDto;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;

public interface OpenViduService {
    DataResDto<?> createSession() throws OpenViduJavaClientException, OpenViduHttpException;
    DataResDto<?> joinSession(SessionJoinReqDto sessionJoinReqDto) throws OpenViduJavaClientException, OpenViduHttpException;
}