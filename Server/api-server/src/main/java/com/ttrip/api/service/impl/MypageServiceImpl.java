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

import java.io.File;
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

    @Override
    public DataResDto<?> updateProfileAndMarkerImg(ProfileUpdateReqDto profileUpdateReqDto, MemberDetails memberDetails) {
        Member member=memberDetails.getMember();

        member= imageUtil.updateProfileImg(profileUpdateReqDto.getProfileImg(),member);
        member=imageUtil.updateMarkerImg(profileUpdateReqDto.getMarkerImg(),member);

        memberRepository.save(member);
        return DataResDto.builder()
                .message("프로필 사진이 업데이트되었습니다.")
                .data(ProfileUpdateResDto.toBuild(member))
                .build();
    }

    @Override
    public DataResDto<?> updateBackgroundImg(BackgroundUpdateReqDto backgroundUpdateReqDto, MemberDetails memberDetails) {
        Member member=memberDetails.getMember();

        MultipartFile backgroundImg=backgroundUpdateReqDto.getBackgroundImg();

        //기존 이미지 삭제
        File rmImg;
        if(member.getBackgroundImgPath()!=null) {
            rmImg = new File(member.getBackgroundImgPath());
            member.setProfileImgPath(imageUtil.removeImg(rmImg));
        }

        //입력된 사진 없음
        if(backgroundImg.isEmpty())
        {
            memberRepository.save(member);
            return DataResDto.builder()
                    .message("등록된 사진이 없습니다.")
                    .data(BackgroundUpdateResDto.toBuild(member))
                    .build();
        }

        imageUtil.checkImageType(backgroundImg);

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
