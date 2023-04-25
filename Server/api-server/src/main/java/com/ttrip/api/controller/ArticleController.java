package com.ttrip.api.controller;

import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.NewArticleParamsDto;
import com.ttrip.api.dto.SearchParamsDto;
import com.ttrip.api.service.ArticleService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Api(tags = "유저 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;
//  dto쓸려고 post요청
    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 조회 성공"),
            @ApiResponse(code = 400, message = "게시글 조회 실패")
    })
    @ApiOperation(value = "게시글 목록 조회 API", httpMethod = "POST")
    @PostMapping("/")
    public DataResDto<?> search(@RequestBody SearchParamsDto searchParamsDto, UUID uuid) {
        return articleService.search(searchParamsDto);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 생성 성공"),
            @ApiResponse(code = 400, message = "게시글 생성 실패")
    })
    @ApiOperation(value = "게시글 생성 API", httpMethod = "POST")
    @PostMapping("/new")
    public DataResDto<?> newArticle(@RequestBody NewArticleParamsDto newArticleParamsDto, UUID uuid) {
        return articleService.newArticle(newArticleParamsDto, uuid);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 상세 조회 성공"),
            @ApiResponse(code = 400, message = "게시글 상세 조회 실패")
    })
    @ApiOperation(value = "게시글 상세 조회 API", httpMethod = "GET")
    @GetMapping("/{articleId}")
    public DataResDto<?> searchDetail(@PathVariable("articleId") Integer articleId, UUID uuid) {
        return articleService.searchDetail(articleId, uuid);
    }
    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 삭제 성공"),
            @ApiResponse(code = 400, message = "게시글 삭제 실패")
    })
    @ApiOperation(value = "게시글 삭제 API", httpMethod = "Delete")
    @DeleteMapping("/{articleId}")
    public DataResDto<?> ereaseArticle(@PathVariable("articleId") Integer articleId, UUID uuid) {
        return articleService.ereaseArticle(articleId, uuid);
    }
}
