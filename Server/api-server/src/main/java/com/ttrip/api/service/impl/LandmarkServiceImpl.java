package com.ttrip.api.service.impl;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.landmarkDto.LandmarkListResDto;
import com.ttrip.api.dto.landmarkDto.ReceiveBadgeReqDto;
import com.ttrip.api.dto.landmarkDto.ReceiveBadgeResDto;
import com.ttrip.api.service.LandmarkService;
import com.ttrip.core.entity.badge.Badge;
import com.ttrip.core.entity.badge.Landmark;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.landmark.BadgeRepository;
import com.ttrip.core.repository.landmark.LandmarkRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LandmarkServiceImpl implements LandmarkService {
    private final LandmarkRepository landmarkRepository;
    private final BadgeRepository badgeRepository;

    /**
     * 랜드마크 목록 데이터들이 반환됩니다.
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

    /**
     * 뱃지를 발급합니다.
     * @param receiveBadgeReqDto : landmarkId
     * @return : badgeId, badgeName, badgeImgPath 데이터
     */
    @Override
    public DataResDto<?> receiveBadge(Member member, ReceiveBadgeReqDto receiveBadgeReqDto) {
        Landmark landmark = landmarkRepository.findByLandmarkId(receiveBadgeReqDto.getLandmarkId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.LANDMARK_NOT_EXIST.getMessage()));

        // 이미 해당 뱃지를 보유하고 있는지 확인
        if(badgeRepository.existsByMemberAndLandmark(member, landmark)) {
            return DataResDto.builder()
                    .status(204)
                    .message("이미 발급한 뱃지입니다.")
                    .build();
        }

        // 뱃지 db 저장
        Badge badge = Badge.builder()
                .member(member)
                .landmark(landmark)
                .build();
        badgeRepository.save(badge);

        ReceiveBadgeResDto receiveBadgeResDto = ReceiveBadgeResDto.builder()
                .badgeId(badge.getBadgeId())
                .badgeName(landmark.getLandmarkName())
                .badgeImgPath(landmark.getBadgeImgPath())
                .build();

        return DataResDto.builder()
                .message("뱃지를 발급했습니다.")
                .data(receiveBadgeResDto)
                .build();
    }

    /**
     * 해당 유저가 가진 뱃지를 조회합니다.
     * @return : badgeId, badgeName, badgeImgPath 데이터
     */
    @Override
    public DataResDto<?> getBadgeList(Member member) {
        List<ReceiveBadgeResDto> badgeList = new ArrayList<>();

        for (Badge badge : badgeRepository.findByMemberMemberId(member.getMemberId())) {
            Landmark landmark = landmarkRepository.findByLandmarkId(badge.getLandmark().getLandmarkId())
                    .orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.LANDMARK_NOT_EXIST.getMessage()));

            ReceiveBadgeResDto receiveBadgeResDto = ReceiveBadgeResDto.builder()
                    .badgeId(badge.getBadgeId())
                    .badgeName(landmark.getLandmarkName())
                    .badgeImgPath(landmark.getBadgeImgPath())
                    .build();
            badgeList.add(receiveBadgeResDto);
        }
        return DataResDto.builder()
                .message(String.format("%s님의 뱃지 목록를 조회했습니다.", member.getNickname()))
                .data(badgeList)
                .build();
    }
}