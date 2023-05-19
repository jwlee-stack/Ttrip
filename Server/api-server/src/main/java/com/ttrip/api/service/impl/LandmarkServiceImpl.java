package com.ttrip.api.service.impl;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.landmarkDto.*;
import com.ttrip.api.exception.BadRequestException;
import com.ttrip.api.service.LandmarkService;
import com.ttrip.core.entity.landmark.Badge;
import com.ttrip.core.entity.landmark.Doodle;
import com.ttrip.core.entity.landmark.Landmark;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.landmark.BadgeRepository;
import com.ttrip.core.repository.landmark.DoodleRepository;
import com.ttrip.core.repository.landmark.LandmarkRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
import com.ttrip.core.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LandmarkServiceImpl implements LandmarkService {
    private final LandmarkRepository landmarkRepository;
    private final BadgeRepository badgeRepository;
    private final DoodleRepository doodleRepository;
    private final ImageUtil imageUtil;

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
        if (badgeRepository.existsByMemberAndLandmark(member, landmark)) {
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

        for (Badge badge : badgeRepository.findByMember(member)) {
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

    /**
     * 랜드마크를 추가합니다.
     * @return : landmarkId, landmarkName, badgeImgPath, latitude, longitude 데이터
     */
    @Override
    @Transactional
    public DataResDto<?> addLandmark(LandmarkReqDto landmarkReqDto) {
        MultipartFile badgeImg = landmarkReqDto.getBadgeImgPath();

        if (badgeImg.isEmpty() || badgeImg.equals(null)) {
            throw new BadRequestException("뱃지 사진이 입력되지 않았습니다.");
        }
        // 타입 체크
        imageUtil.checkImageType(badgeImg);

        try {
            Landmark landmark = Landmark.builder()
                    .landmarkName(landmarkReqDto.getLandmarkName())
                    .badgeImgPath(imageUtil.saveBadgeImg(landmarkReqDto.getLandmarkName(), badgeImg, "badgeImg"))
                    .latitude(landmarkReqDto.getLatitude())
                    .longitude(landmarkReqDto.getLongitude())
                    .build();
            landmarkRepository.save(landmark);

            return DataResDto.builder()
                    .message("랜드마크 저장이 완료되었습니다.")
                    .data(LandmarkResDto.toBuild(landmark))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("랜드마크 저장에 실패하였습니다.");
        }
    }

    /**
     * 낙서를 저장합니다.
     * @return : doodleId, latitude, longitude, positionX, positionY, positionZ, doodleImgPath, landmarkId 데이터
     */
    @Override
    public DataResDto<?> saveDoodle(Member member, DoodleReqDto doodleReqDto) {
        MultipartFile doodleImg = doodleReqDto.getDoodleImgPath();

        if (doodleImg.isEmpty() || doodleImg.equals(null)) {
            throw new BadRequestException("낙서 사진이 입력되지 않았습니다.");
        }
        // 타입 체크
        imageUtil.checkImageType(doodleImg);
        Landmark landmark = landmarkRepository.findByLandmarkId(doodleReqDto.getLandmarkId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.LANDMARK_NOT_EXIST.getMessage()));

        try {
            String imgPath = imageUtil.saveBadgeImg(landmark.getLandmarkName() + "_"
                            +  member.getNickname(), doodleImg, "doodleImg");
            Doodle doodle = Doodle.builder()
                    .latitude(doodleReqDto.getLatitude())
                    .longitude(doodleReqDto.getLongitude())
                    .positionX(doodleReqDto.getPositionX())
                    .positionY(doodleReqDto.getPositionY())
                    .positionZ(doodleReqDto.getPositionZ())
                    .doodleImgPath(imgPath)
                    .landmark(landmark)
                    .build();
            doodleRepository.save(doodle);
            return DataResDto.builder()
                    .message("낙서 저장이 완료되었습니다.")
                    .data(DoodleResDto.toBuild(doodle, doodleRepository.findByLandmarkAndDoodleImgPath(landmark, imgPath).get().getDoodleId()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("낙서 저장에 실패하였습니다.");
        }
    }

    /**
     * 해당 랜드마크의 낙서 목록을 조회합니다.
     * @return : doodleId, latitude, longitude, positionX, positionY, positionZ, doodleImgPath, landmarkId 데이터를 담은 리스트
     */
    @Override
    public DataResDto<?> getDoodleList(Integer landmarkId) {
        List<DoodleResDto> doodleList = new ArrayList<>();

        for (Doodle doodle : doodleRepository.findByLandmarkLandmarkId(landmarkId)) {
            DoodleResDto doodleResDto = DoodleResDto.builder()
                    .doodleId(doodle.getDoodleId())
                    .latitude(doodle.getLatitude())
                    .longitude(doodle.getLongitude())
                    .positionX(doodle.getPositionX())
                    .positionY(doodle.getPositionY())
                    .positionZ(doodle.getPositionZ())
                    .doodleImgPath(doodle.getDoodleImgPath())
                    .landmarkId(landmarkId)
                    .build();
            doodleList.add(doodleResDto);
        }
        return DataResDto.builder()
                .message(String.format("%s의 낙서 목록를 조회했습니다.", landmarkRepository.findByLandmarkId(landmarkId).get().getLandmarkName()))
                .data(doodleList)
                .build();
    }
}