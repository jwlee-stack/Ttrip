package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.NewArticleReqDto;
import com.ttrip.api.dto.SearchReqDto;

import java.util.UUID;

public interface ArticleService {
    DataResDto<?> search(SearchReqDto searchReqDto);
    DataResDto<?> newArticle(NewArticleReqDto newArticleReqDto);
    DataResDto<?> searchDetail(Integer articleId, UUID uuid);
    DataResDto<?> ereaseArticle(Integer articleId, UUID uuid);
}
