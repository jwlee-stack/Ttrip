package com.ttrip.api.service.impl;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.artticleDto.*;
import com.ttrip.api.dto.fcmMessageDto.FcmMessageReqDto;
import com.ttrip.api.exception.BadRequestException;
import com.ttrip.api.exception.NotFoundException;
import com.ttrip.api.exception.UnauthorizationException;
import com.ttrip.api.service.ArticleService;
import com.ttrip.api.service.FcmService;
import com.ttrip.core.entity.applyArticle.ApplyArticle;
import com.ttrip.core.entity.article.Article;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.applyArticle.ApplyArticleRepository;
import com.ttrip.core.repository.article.ArticleRepository;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
import com.ttrip.core.utils.EuclideanDistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final MemberRepository memberRepository;

    private final FcmService fcmService;

    private final ApplyArticleRepository applyArticleRepository;

    private final EuclideanDistanceUtil euclideanDistanceUtil;

    @Override
    public DataResDto<?> search(SearchReqDto searchReqDto) {

        Integer condition = searchReqDto.getCondition();
        String nation = searchReqDto.getNation();
        String city = searchReqDto.getCity();
        String keyword = searchReqDto.getKeyword();

        List<Article> articleList;
        List<SearchResDto> searchResultDtoList = new ArrayList<>();

        if (condition == null) {
            throw new BadRequestException(ErrorMessageEnum.UNEXPECT_VALUE.getMessage());
        }
        if (condition == 0) {
//            전체조회
            articleList = articleRepository.findAllByOrderByCreatedAtDesc();
        } else if (condition == 1) {
            if (city != null) {
//                도시로 조회
                articleList = articleRepository.findByCityOrderByCreatedAtDesc(city);
            } else {
//                나라로 조회
                articleList = articleRepository.findByNationOrderByCreatedAtDesc(nation);
            }
        } else if (condition == 2) {
//            keyword로 조회
            articleList = articleRepository.findByTitleContainingOrContentContainingOrderByCreatedAtDesc(keyword, keyword);
        } else {
            throw new BadRequestException(ErrorMessageEnum.UNEXPECT_VALUE.getMessage());
        }
//      돌면서 dto만듬  조회되지않으면 빈 배열 갑니당
        for (Article article : articleList) {
            //삭제 되지 않으면
            if (article.getStatus() != 'D') {
                searchResultDtoList.add(SearchResDto.toBuild(article));
            }
        }
        return DataResDto.builder().message("게시글 목록을 검색했습니다.").data(searchResultDtoList).build();
    }


    @Override
    public DataResDto<?> newArticle(NewArticleReqDto newArticleReqDto, UUID memberUuid) {
        try {
            Member member = memberRepository.findByMemberUuid(memberUuid).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.USER_NOT_EXIST.getMessage()));
            Article article = Article.builder()
                    .title(newArticleReqDto.getTitle())
                    .content(newArticleReqDto.getContent())
                    .city(newArticleReqDto.getCity())
                    .nation(newArticleReqDto.getNation())
                    .startDate(newArticleReqDto.getStartDate())
                    .endDate(newArticleReqDto.getEndDate())
                    .member(member)
                    .status('T')
                    .build();

            Article savedArticle = articleRepository.save(article);

            NewArticleResDto newArticleResultDto = new NewArticleResDto();
            newArticleResultDto.setArticleId(savedArticle.getArticleId());

            return DataResDto.builder().message("게시글 등록이 완료되었습니다.").data(newArticleResultDto).build();
        } catch (Exception e) {
            throw new BadRequestException(ErrorMessageEnum.SERVER_ERROR.getMessage());
        }

    }

    @Override
    public DataResDto<?> searchDetail(Integer articleId, Member member) {
        try {
            Article article = articleRepository.findByArticleId(articleId).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.ARTICLE_NOT_EXIST.getMessage()));

            DetailResDto searchDetailDto = DetailResDto.builder()
                    .articleId(articleId)
                    .authorName(article.getMember().getNickname())
                    .imgPath(article.getMember().getProfileImgPath())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .nation(article.getNation())
                    .city(article.getCity())
                    .startDate(article.getStartDate())
                    .endDate(article.getEndDate())
                    .createdAt(article.getCreatedAt())
                    .status(article.getStatus())
                    .isMine(member.equals(article.getMember()))
                    .isApplied(applyArticleRepository.existsByArticleAndMember(article, member))
                    .similarity(euclideanDistanceUtil.getMatchingRate(member.getSurvey(), article.getMember().getSurvey()))
                    .searchApplyResDtoList(searchApply(article, member.getMemberUuid()))
                    .build();

            return DataResDto.builder().message("게시글 목록이 조회되었습니다.").data(searchDetailDto).build();
        } catch (Exception e) {
            throw new BadRequestException(ErrorMessageEnum.SERVER_ERROR.getMessage());
        }
    }

    @Override
    public DataResDto<?> eraseArticle(Integer articleId, UUID memberUuid) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.ARTICLE_NOT_EXIST.getMessage()));
        if (!article.getMember().getMemberUuid().equals(memberUuid)) {
            throw new UnauthorizationException(ErrorMessageEnum.NO_AUTH.getMessage());
        }
        try {
            article.setStatus('D');
            articleRepository.save(article);
            return DataResDto.builder().message("게시글이 삭제되었습니다.").data(true).build();
        } catch (Exception e) {
            throw new BadRequestException(ErrorMessageEnum.SERVER_ERROR.getMessage());
        }
    }

    @Override
    public DataResDto<?> newApply(ApplyReqDto applyReqDto, UUID memberUuid) {
        Article article = articleRepository.findById(applyReqDto.getArticleId()).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.ARTICLE_NOT_EXIST.getMessage()));
        Member member = memberRepository.findByMemberUuid(memberUuid).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.USER_NOT_EXIST.getMessage()));
        if (article.getMember().getMemberUuid().equals(memberUuid))
            throw new UnauthorizationException(ErrorMessageEnum.NO_AUTH.getMessage());

        try {
            ApplyArticle applyArticle = ApplyArticle.builder()
                    .article(article)
                    .status('T')
                    .member(member)
                    .requestContent(applyReqDto.getRequestContent())
                    .build();
            applyArticleRepository.save(applyArticle);

            //게시글 작성자의 fcm 토큰이 있으면 메세지 발송
            fcmService.sendMessageTo(member, FcmMessageReqDto.builder()
                    .type(2)
                    .targetUuid(article.getMember().getMemberUuid())
                    .extraId(article.getArticleId().toString())
                    .extraData("")
                    .build()
            );


            return DataResDto.builder().message("신청이 완료되었습니다.").data(true).build();
        } catch (Exception e) {
            throw new BadRequestException(ErrorMessageEnum.SERVER_ERROR.getMessage());
        }
    }

    private List<SearchApplyResDto> searchApply(Article article, UUID memberUuid) {
        List<SearchApplyResDto> searchApplyResDtoList = new ArrayList<>();
        // 내 게시긇이면 다른사람 매칭신청을 다볼수있다
        if (article.getMember().getMemberUuid().equals(memberUuid)) {
            for (ApplyArticle applyArticle : applyArticleRepository.findByArticle(article)) {
                SearchApplyResDto searchApplyResDto = SearchApplyResDto.builder()
                        .applyId(applyArticle.getApplyArticleId())
                        .applicantNickname(applyArticle.getMember().getNickname())
                        .applicantUuid(applyArticle.getMember().getMemberUuid())
                        .requestContent(applyArticle.getRequestContent())
                        .imgPath(applyArticle.getMember().getProfileImgPath())
                        .similarity(euclideanDistanceUtil.getMatchingRate(article.getMember().getSurvey(), applyArticle.getMember().getSurvey()))
                        .build();
                searchApplyResDtoList.add(searchApplyResDto);
            }
            return searchApplyResDtoList;
        } else {
//            내 게시글이 아닌경우 내 매칭 신청글만 볼수있음!
            Member member = memberRepository.findByMemberUuid(memberUuid).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.USER_NOT_EXIST.getMessage()));
            Optional<ApplyArticle> optionalApplyArticle = applyArticleRepository.findByArticleAndMember(article, member);
            if (optionalApplyArticle.isPresent()) {
                ApplyArticle applyArticle = optionalApplyArticle.get();
                SearchApplyResDto searchApplyResDto = SearchApplyResDto.builder()
                        .applyId(applyArticle.getApplyArticleId())
                        .applicantNickname(applyArticle.getMember().getNickname())
                        .applicantUuid(applyArticle.getMember().getMemberUuid())
                        .requestContent(applyArticle.getRequestContent())
                        .imgPath(applyArticle.getMember().getProfileImgPath())
                        .similarity(100)
                        .build();
                searchApplyResDtoList.add(searchApplyResDto);
            }
            return searchApplyResDtoList;
        }
    }

    @Override
    public DataResDto<?> endArticle(Integer articleId, UUID memberUuid) {
        Member member = memberRepository.findByMemberUuid(memberUuid).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.USER_NOT_EXIST.getMessage()));
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (!optionalArticle.isPresent()) {
            throw new NotFoundException(ErrorMessageEnum.ARTICLE_NOT_EXIST.getMessage());
        }
        Article article = optionalArticle.get();
        if (member.equals(article.getMember()) && article.getStatus() == 'T') {
            article.setStatus('F');
            articleRepository.save(article);
            return DataResDto.builder().message("모집이 종료되었습니다.").data(true).build();
        } else {
            throw new BadRequestException(ErrorMessageEnum.NO_AUTH.getMessage());
        }
    }
}
