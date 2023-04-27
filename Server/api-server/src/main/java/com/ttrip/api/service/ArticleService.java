package com.ttrip.api.service;

import com.ttrip.api.dto.*;

import java.util.UUID;

public interface ArticleService {
    DataResDto<?> search(SearchReqDto searchReqDto);
    DataResDto<?> newArticle(NewArticleReqDto newArticleReqDto, UUID memberUuid);
    DataResDto<?> searchDetail(Integer articleId, UUID memberUuid);
    DataResDto<?> eraseArticle(Integer articleId, UUID memberUuid);
    DataResDto<?> newApply(ApplyReqDto applyReqDto, UUID memberUuid);
    DataResDto<?> searchApply(Integer articleId, UUID memberUuid);
    DataResDto<?> endArticle(Integer articleId, UUID memberUuid);
}
