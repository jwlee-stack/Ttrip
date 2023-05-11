package com.ttrip.api.service.impl;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.landmarkDto.LandmarkListResDto;
import com.ttrip.api.service.LandmarkService;
import com.ttrip.core.entity.badge.Landmark;
import com.ttrip.core.repository.landmark.LandmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LandmarkServiceImpl implements LandmarkService {
    private final LandmarkRepository landmarkRepository;

    /**
     * 랜드마크 목록 데이터들이 반환됩니다.
     *
     * @return : landmarkid, landmarkname, latitude, longitude 데이터를 담은 리스트
     */
    @Override
    public DataResDto<?> getLandmarkList() {
        List<LandmarkListResDto> landmarkList = new ArrayList<>();

        for (Landmark landmark : landmarkRepository.findAll()) {
            LandmarkListResDto landmarkListResDto = LandmarkListResDto.builder()
                    .landmarkId(landmark.getLandmarkId())
                    .landmarkName(landmark.getLandmarkName())
                    .latitude(landmark.getLatitude())
                    .longitude(landmark.getLongitude())
                    .build();
            landmarkList.add(landmarkListResDto);
        }

        return DataResDto.builder()
                .message("랜드마크를 조회했습니다.")
                .data(landmarkList)
                .build();
    }
}