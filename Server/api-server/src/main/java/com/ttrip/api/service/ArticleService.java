package com.ttrip.api.service;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.NewArticleParamsDto;
import com.ttrip.api.dto.SearchParamsDto;

import java.util.UUID;

public interface ArticleService {
    DataResDto<?> search(SearchParamsDto searchParamsDto);
    DataResDto<?> newArticle(NewArticleParamsDto newArticleParamsDto);
    DataResDto<?> searchDetail(Integer articleId, UUID uuid);
    DataResDto<?> ereaseArticle(Integer articleId, UUID uuid);
}
