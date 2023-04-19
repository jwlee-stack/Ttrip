package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;

public interface MemberService {
    DataResDto<?> findMemberById(Integer id);
}
