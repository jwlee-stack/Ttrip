package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;

import java.util.UUID;

public interface LiveService {
    DataResDto<?> getMembersInCity(String city, double lng, double lat);
}
