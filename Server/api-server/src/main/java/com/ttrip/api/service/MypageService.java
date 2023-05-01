package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.service.impl.MemberDetails;

public interface MypageService {
    DataResDto<?> viewMyArticles(MemberDetails memberDetails);
}
