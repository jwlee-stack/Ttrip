package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.landmarkDto.ReceiveBadgeReqDto;
import com.ttrip.api.service.impl.MemberDetails;

public interface LandmarkService {
    DataResDto<?> getLandmarkList();
    DataResDto<?> receiveBadge(MemberDetails memberDetails, ReceiveBadgeReqDto receiveBadgeReqDto);
}