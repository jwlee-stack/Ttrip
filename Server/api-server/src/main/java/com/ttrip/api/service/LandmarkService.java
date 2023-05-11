package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;

public interface LandmarkService {
    DataResDto<?> getLandmarkList();
}