package com.ttrip.api.service.impl;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.SearchResDto;
import com.ttrip.api.service.MypageService;
import com.ttrip.core.entity.article.Article;
import com.ttrip.core.entity.member.Member;
import com.ttrip.core.repository.article.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MypageServiceImpl implements MypageService {
    private final ArticleRepository articleRepository;

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
}
