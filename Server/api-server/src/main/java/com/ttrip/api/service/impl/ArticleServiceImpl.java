package com.ttrip.api.service.impl;

import com.ttrip.api.dto.*;
import com.ttrip.api.service.ArticleService;
import com.ttrip.core.entity.article.Article;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.article.ArticleRepository;
import com.ttrip.core.repository.member.MemberRepository;
import com.ttrip.core.utils.ErrorMessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final MemberRepository memberRepository;
    @Override
    public DataResDto<?> search(SearchReqDto searchReqDto) {

        Integer condition = searchReqDto.getCondition();
        String nation = searchReqDto.getNation();
        String city = searchReqDto.getCity();
        String keyword = searchReqDto.getKeyword();

        List<Article> articleList;
        List<SearchRestDto> searchResultDtoList = new ArrayList<>();

        if (condition == null) {
            return DataResDto.builder().message("잘못된 요청입니다.").status(400).data(searchResultDtoList).build();
        }
        if (condition == 0) {
//            전체조회
            articleList = articleRepository.findAll();
        } else if (condition == 1) {
            if (city != null ) {
//                도시로 조회
                articleList = articleRepository.findByCity(city);
            }else {
//                나라로 조회
                articleList = articleRepository.findByNation(nation);
            }
        } else if (condition == 2){
//            keyword로 조회
            articleList = articleRepository.findByTitleOrContentContaining(keyword, keyword);
        } else {
            return DataResDto.builder().message("잘못된 요청입니다.").status(400).data(searchResultDtoList).build();
        }
//      돌면서 dto만듬  조회되지않으면 빈 배열 갑니당
        for (Article article : articleList){
            SearchRestDto searchResultDto = SearchRestDto.builder()
                    .articleId(article.getId())
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
        return DataResDto.builder().message("게시글 목록이 조회되었습니다.").data(searchResultDtoList).build();
    }

    @Override
    public DataResDto<?> newArticle(NewArticleReqDto newArticleReqDto) {
        try {
            UUID memberUuid = newArticleReqDto.getUuid();
            Member member = memberRepository.findByUuid(memberUuid).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.USER_NOT_EXIST.getMessage()));
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
            newArticleResultDto.setArticleId(savedArticle.getId());

            return DataResDto.builder().message("게시글 등록이 완료되었습니다.").data(newArticleResultDto).build();
        }catch (Exception e){
            return DataResDto.builder().message("게시글 등록에 실패했습니다.").status(400).build();
        }

    }

    @Override
    public DataResDto<?> searchDetail(Integer articleId, UUID uuid) {
        try {
            Article article = articleRepository.findById(articleId).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.ARTICLE_NOT_EXIST.getMessage()));
            Boolean isMine = uuid.equals(article.getMember().getUuid()) ? true : false;

            DetailResDto searchDetailDto = DetailResDto.builder()
                    .articleId(articleId)
                    .authorName(article.getMember().getNickname())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .nation(article.getNation())
                    .city(article.getCity())
                    .startDate(article.getStartDate())
                    .endDate(article.getEndDate())
                    .createdAt(article.getCreatedAt())
                    .status(article.getStatus())
                    .isMine(isMine)
                    .build();

            return DataResDto.builder().message("게시글 목록이 조회되었습니다.").data(searchDetailDto).build();
        }catch (Exception e){
            return DataResDto.builder().message("게시글 목록이 조회실패.").status(400).build();
        }
    }

    @Override
    @Transactional
    public DataResDto<?> ereaseArticle(Integer articleId, UUID memberUuid) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.ARTICLE_NOT_EXIST.getMessage()));
        if (!article.getMember().getUuid().equals(memberUuid)){
            return DataResDto.builder().message("게시글 삭제:잘못된 접근입니다.").status(400).data(false).build();
        }
        try {

            articleRepository.delete(article);
            return DataResDto.builder().message("게시글이 삭제되었습니다.").data(true).build();
        }catch (Exception e){
            return DataResDto.builder().message("게시글 삭제 실패.").status(400).data(false).build();
        }
    }
}
