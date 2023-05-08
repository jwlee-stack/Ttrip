package com.ttrip.api.service.impl;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.artticleDto.SearchResDto;
import com.ttrip.api.dto.mypageDto.mypageReqDto.BackgroundUpdateReqDto;
import com.ttrip.api.dto.mypageDto.mypageReqDto.InfoUpdateReqDto;
import com.ttrip.api.dto.mypageDto.mypageReqDto.ProfileUpdateReqDto;
import com.ttrip.api.dto.mypageDto.mypageResDto.BackgroundUpdateResDto;
import com.ttrip.api.dto.mypageDto.mypageResDto.InfoUpdateResDto;
import com.ttrip.api.dto.mypageDto.mypageResDto.ProfileUpdateResDto;
import com.ttrip.api.exception.BadRequestException;
import com.ttrip.api.service.MypageService;
import com.ttrip.core.customEnum.Gender;
import com.ttrip.core.entity.article.Article;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.article.ArticleRepository;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MypageServiceImpl implements MypageService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ImageUtil imageUtil;
    @Override
    public DataResDto<?> viewMyArticles(MemberDetails memberDetails) {
        Member member = memberDetails.getMember();
        List<Article> myArticleList = articleRepository.findAllByMember(member);
        List<SearchResDto> searchResultDtoList = new ArrayList<>();

        for (Article article : myArticleList) {
            searchResultDtoList.add(SearchResDto.toBuild(article));
        }
        return DataResDto.builder()
                .message("내 게시글 조회 성공")
                .data(searchResultDtoList)
                .build();
    }

    @Override
    public DataResDto<?> updateMember(InfoUpdateReqDto infoUpdateReqDto, MemberDetails memberDetails) {
        log.info("닉네임: {}",infoUpdateReqDto.getNickname());
        log.info("성별: {}",Gender.valueOf(infoUpdateReqDto.getGender()));
        log.info("나이: {}",infoUpdateReqDto.getAge());
        log.info("인트로: {}",infoUpdateReqDto.getIntro());

        if (infoUpdateReqDto.getNickname().isEmpty() || infoUpdateReqDto.getGender() == null || infoUpdateReqDto.getAge() == null)
            throw new BadRequestException("멤버 필수 정보가 누락됐습니다. (닉네임/성별/나이)");

        Member member = memberDetails.getMember();

        //닉네임, 성별, 나이, 인트로 변경//
        member.setNickname(infoUpdateReqDto.getNickname());
        member.setGender(Gender.valueOf(infoUpdateReqDto.getGender()));
        member.setAge(Integer.parseInt(infoUpdateReqDto.getAge()));
        member.setIntro(infoUpdateReqDto.getIntro().isEmpty() ? "20자 이내로 입력해주세요" : infoUpdateReqDto.getIntro());

        memberRepository.save(member);
        return DataResDto.builder()
                .message("회원 정보가 업데이트되었습니다.")
                .data(InfoUpdateResDto.toBuild(member))
                .build();
    }

    //프로필과 마커 이미지 업데이트//
    @Override
    public DataResDto<?> updateProfileAndMarkerImg(ProfileUpdateReqDto profileUpdateReqDto, MemberDetails memberDetails) {
        Member member=memberDetails.getMember();

        member= updateProfileImg(profileUpdateReqDto.getProfileImg(),member);
        member= updateMarkerImg(profileUpdateReqDto.getMarkerImg(),member);

        memberRepository.save(member);
        return DataResDto.builder()
                .message("프로필 사진이 업데이트되었습니다.")
                .data(ProfileUpdateResDto.toBuild(member))
                .build();
    }

    //프로필 사진 업데이트//
    public Member updateProfileImg(MultipartFile profileImg, Member member) {
        log.info("프로필 사진:{}", profileImg);

        //입력된 사진 없음
        if(profileImg.isEmpty())
            throw new BadRequestException("프로필 사진이 입력되지 않았습니다.");

        //넘어온 파일이 이미지인지?
        imageUtil.checkImageType(profileImg);

        //기존 이미지 삭제
        if (!member.getProfileImgPath().isEmpty()) {
            member.setProfileImgPath(imageUtil.removeImg(member.getProfileImgPath()));
        }


        try {
            //프로필 사진 저장
            member.setProfileImgPath(imageUtil.saveImg(member, profileImg, "profileImg"));
        } catch (Exception e) {
            throw new RuntimeException("프로필 사진 변경을 실패했습니다.");
        }

        return member;
    }

    //마커 사진 업데이트//
    public Member updateMarkerImg(MultipartFile markerImg, Member member) {
        log.info("마커 사진:{}", markerImg);

        //입력된 사진 없음
        if(markerImg.isEmpty())
            throw new BadRequestException("마커 사진이 입력되지 않았습니다.");

        //넘어온 파일이 이미지인지?
        imageUtil.checkImageType(markerImg);

        //기존 이미지 삭제
        if (!member.getMarkerImgPath().isEmpty()) {
            member.setMarkerImgPath(imageUtil.removeImg(member.getMarkerImgPath()));
        }

        try {
            //마커 사진 저장
            member.setMarkerImgPath(imageUtil.saveImg(member, markerImg, "markerImg"));
        } catch (Exception e) {
            throw new RuntimeException("마커 사진 변경을 실패했습니다.");
        }

        return member;
    }

    @Override
    public DataResDto<?> updateBackgroundImg(BackgroundUpdateReqDto backgroundUpdateReqDto, MemberDetails memberDetails) {
        Member member=memberDetails.getMember();

        MultipartFile backgroundImg=backgroundUpdateReqDto.getBackgroundImg();

        //입력된 사진 없음
        if(backgroundImg.isEmpty())
            throw new BadRequestException("배경 사진이 입력되지 않았습니다.");

        //넘어온 파일이 이미지인지?
        imageUtil.checkImageType(backgroundImg);

        //기존 이미지 삭제
        if (!member.getBackgroundImgPath().isEmpty()) {
            member.setBackgroundImgPath(imageUtil.removeImg(member.getBackgroundImgPath()));
        }

        try
        {
            member.setBackgroundImgPath(imageUtil.saveImg(member, backgroundImg, "backgroundImg"));
            memberRepository.save(member);
        }
        catch(Exception e)
        {
            throw new RuntimeException("배경 사진 변경을 실패했습니다.");
        }

        return DataResDto.builder()
                .message("배경 사진이 업데이트되었습니다.")
                .data(BackgroundUpdateResDto.toBuild(member))
                .build();
    }


}
