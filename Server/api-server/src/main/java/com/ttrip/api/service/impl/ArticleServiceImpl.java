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
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final MemberRepository memberRepository;
    @Override
    public DataResDto<?> search(SearchParamsDto searchParamsDto) {

        Integer condition = searchParamsDto.getCondition();
        String nation = searchParamsDto.getNation();
        String city = searchParamsDto.getCity();
        String keyword = searchParamsDto.getKeyword();

        List<Article> articleList;
        List<SearchResultDto> searchResultDtoList = new ArrayList<>();

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
        } else {
//            keyword로 조회
            articleList = articleRepository.findByTitleContaining(keyword);
        }
//      돌면서 dto만듬  조회되지않으면 빈 배열 갑니당
        for (Article article : articleList){
            SearchResultDto searchResultDto = new SearchResultDto();

            searchResultDto.setArticleId(article.getId());
            searchResultDto.setAuthorName(article.getMember().getNickname());
            searchResultDto.setTitle(article.getTitle());
            searchResultDto.setContent(article.getContent());
            searchResultDto.setNation(article.getNation());
            searchResultDto.setCity(article.getCity());
            searchResultDto.setStartDate(article.getStartDate());
            searchResultDto.setEndDate(article.getEndDate());
            searchResultDto.setCreatedDate(article.getCreatedDate());
            searchResultDto.setStatus(article.getStatus());

            searchResultDtoList.add(searchResultDto);
        }
        return DataResDto.builder().message("게시글 목록이 조회되었습니다.").status(200).data(searchResultDtoList).build();
    }

    @Override
    public DataResDto<?> newArticle(NewArticleParamsDto newArticleParamsDto, UUID uuid) {
        try {
            String title = newArticleParamsDto.getTitle();
            String content = newArticleParamsDto.getContent();
            String city = newArticleParamsDto.getCity();
            String nation = newArticleParamsDto.getNation();
            LocalDateTime startDate = newArticleParamsDto.getStartDate();
            LocalDateTime endDate = newArticleParamsDto.getEndDate();
            Member member = memberRepository.findByUuid(uuid).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.USER_NOT_EXIST.getMessage()));
            Article article = new Article(title, content, nation, city, startDate, endDate, member);
            Article savedArticle = articleRepository.save(article);

            NewArticleResultDto newArticleResultDto = new NewArticleResultDto();

            newArticleResultDto.setArticleId(savedArticle.getId());

            return DataResDto.builder().message("게시글 등록이 완료되었습니다.").status(200).data(newArticleResultDto).build();
        }catch (Exception e){
            return DataResDto.builder().message("게시글 등록에 실패했습니다.").status(400).build();
        }

    }

    @Override
    public DataResDto<?> searchDetail(Integer articleId, UUID uuid) {
        try {
            Article article = articleRepository.findById(articleId).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.ARTICLE_NOT_EXIST.getMessage()));

            SearchResultDto searchResultDto = new SearchResultDto();

            searchResultDto.setArticleId(article.getId());
            searchResultDto.setAuthorName(article.getMember().getNickname());
            searchResultDto.setTitle(article.getTitle());
            searchResultDto.setContent(article.getContent());
            searchResultDto.setNation(article.getNation());
            searchResultDto.setCity(article.getCity());
            searchResultDto.setStartDate(article.getStartDate());
            searchResultDto.setEndDate(article.getEndDate());
            searchResultDto.setCreatedDate(article.getCreatedDate());
            searchResultDto.setStatus(article.getStatus());

            return DataResDto.builder().message("게시글 목록이 조회되었습니다.").status(200).data(searchResultDto).build();
        }catch (Exception e){
            return DataResDto.builder().message("게시글 목록이 조회실패.").status(400).build();
        }
    }

    @Override
    @Transactional
    public DataResDto<?> ereaseArticle(Integer articleId, UUID uuid) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NoSuchElementException(ErrorMessageEnum.ARTICLE_NOT_EXIST.getMessage()));
        if (!article.getMember().getUuid().equals(uuid)){
            return DataResDto.builder().message("게시글 삭제:잘못된 접근입니다.").status(400).build();
        }
        try {

            articleRepository.delete(article);
            return DataResDto.builder().message("게시글이 삭제되었습니다.").status(200).data(true).build();
        }catch (Exception e){
            return DataResDto.builder().message("게시글 삭제 실패.").status(400).build();
        }
    }
}
