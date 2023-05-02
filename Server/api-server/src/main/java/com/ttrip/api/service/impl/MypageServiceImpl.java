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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MypageServiceImpl implements MypageService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Override
    public DataResDto<?> viewMyArticles(MemberDetails memberDetails) {
        Member member = memberDetails.getMember();
        List<Article> myArticleList = articleRepository.findAllByMember(member);
        List<SearchResDto> searchResultDtoList = new ArrayList<>();

        //코드 중복으로 노란 밑줄 생기는데, toBuild로 묶으면 어떨까요?
        for (Article article : myArticleList) {
            SearchResDto searchResultDto = SearchResDto.builder()
                    .articleId(article.getArticleId())
                    .authorName(article.getMember().getNickname())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .nation(article.getNation())
                    .city(article.getCity())
                    .status(article.getStatus())
                    .startDate(article.getStartDate())
                    .endDate(article.getEndDate())
                    .build();

            searchResultDtoList.add(searchResultDto);
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
        member.setAge(infoUpdateReqDto.getAge());
        member.setIntro(infoUpdateReqDto.getIntro().isEmpty() ? "20자 이내로 입력해주세요" : infoUpdateReqDto.getIntro());

        memberRepository.save(member);
        return DataResDto.builder()
                .message("회원 정보가 업데이트되었습니다.")
                .data(InfoUpdateResDto.toBuild(member))
                .build();
    }

    @Override
    public DataResDto<?> updateProfileImg(ProfileUpdateReqDto profileUpdateReqDto, MemberDetails memberDetails) {
        Member member=memberDetails.getMember();

        try
        {
            member.setProfileImgPath(changeImg(member, profileUpdateReqDto.getProfileImg(), "profileImg"));
            member.setMarkerImgPath(changeImg(member, profileUpdateReqDto.getMarkerImg(), "markerImg"));
            if(!member.getNickname().isEmpty())
                memberRepository.save(member);
        }
        catch(Exception e)
        {
            throw new RuntimeException("프로필 사진 변경을 실패했습니다.");
        }

        return DataResDto.builder()
                .message("프로필 사진이 업데이트되었습니다.")
                .data(ProfileUpdateResDto.toBuild(member))
                .build();
    }

    @Override
    public DataResDto<?> updateBackgroundImg(BackgroundUpdateReqDto backgroundUpdateReqDto, MemberDetails memberDetails) {
        Member member=memberDetails.getMember();

        try
        {
            member.setBackgroundImgPath(changeImg(member, backgroundUpdateReqDto.getBackgroundImg(), "backgroundImg"));
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

    public String changeImg(Member member, MultipartFile img, String folder) throws IOException {
        //이미지 변경//
        String path = System.getProperty("user.dir") + File.separator + folder + File.separator; //공통 경로
        String customImgPath = path + member.getMemberUuid() + ".png"; //변경 이미지 경로
        String defaultImgPath = path + "default.png"; //디폴트 이미지 경로
        File profileImg = new File(customImgPath); //파일 객체 생성

        log.info("공통 경로: {}",path);
        log.info("기본 이미지 경로: {}",defaultImgPath);
        log.info("변경 이미지 경로: {}",customImgPath);
        log.info("이미지 파일: {}",img);

        //사용자가 이미지를 설정했는지?
        if (!img.isEmpty()) //이미지 설정함
        {
            profileImg.mkdirs();
            img.transferTo(profileImg); //이미지 저장
        }
        else //이미지 설정 안 함
        {
            profileImg.delete();//기존 이미지 삭제
            customImgPath = defaultImgPath; //디폴트 이미지로 변경
        }
        return customImgPath;
    }
}
