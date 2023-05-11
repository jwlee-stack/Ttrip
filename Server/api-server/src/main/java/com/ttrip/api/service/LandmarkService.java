package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.landmarkDto.ReceiveBadgeReqDto;
import com.ttrip.core.entity.member.Member;

public interface LandmarkService {
    DataResDto<?> getLandmarkList();
    DataResDto<?> receiveBadge(Member member, ReceiveBadgeReqDto receiveBadgeReqDto);
}