package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.core.entity.member.Member;

import java.util.UUID;

public interface LiveService {
    DataResDto<?> getMembersInCity(Member member, String city, double lng, double lat);
}
