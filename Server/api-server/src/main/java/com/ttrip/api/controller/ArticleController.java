package com.ttrip.api.controller;

import com.ttrip.api.dto.ApplyReqDto;
import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.NewArticleReqDto;
import com.ttrip.api.dto.SearchReqDto;
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
    public DataResDto<?> search(@RequestBody SearchReqDto searchReqDto) {
        return articleService.search(searchReqDto);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 생성 성공"),
            @ApiResponse(code = 400, message = "게시글 생성 실패")
    })
    @ApiOperation(value = "게시글 생성 API", httpMethod = "POST")
    @PostMapping("/new")
    public DataResDto<?> newArticle(@RequestBody NewArticleReqDto newArticleReqDto) {
        return articleService.newArticle(newArticleReqDto);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 상세 조회 성공"),
            @ApiResponse(code = 400, message = "게시글 상세 조회 실패")
    })
    @ApiOperation(value = "게시글 상세 조회 API", httpMethod = "GET")
    @GetMapping("/{articleId}/{uuid}")
    public DataResDto<?> searchDetail(@PathVariable("articleId") Integer articleId, @PathVariable("uuid") UUID uuid) {
        return articleService.searchDetail(articleId, uuid);
    }
    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 삭제 성공"),
            @ApiResponse(code = 400, message = "게시글 삭제 실패")
    })
    @ApiOperation(value = "게시글 삭제 API", httpMethod = "Delete")
    @DeleteMapping("/{articleId}/{uuid}")
    public DataResDto<?> ereaseArticle(@PathVariable("articleId") Integer articleId, @PathVariable("uuid") UUID uuid) {
        return articleService.ereaseArticle(articleId, uuid);
    }
    @ApiResponses({
            @ApiResponse(code = 200, message = "신청이 완료되었습니다."),
            @ApiResponse(code = 400, message = "매칭 참여 신청 실패")
    })
    @ApiOperation(value = "매칭 참여 신청", httpMethod = "POST")
    @PostMapping("/newApply")
    public DataResDto<?> newApply(@RequestBody ApplyReqDto applyReqDto) {
        return articleService.newApply(applyReqDto);
    }
    @ApiResponses({
            @ApiResponse(code = 200, message = "신청한 유저 목록이 조회되었습니다"),
            @ApiResponse(code = 400, message = "신청한 유저 목록이 조회 실패")
    })
    @ApiOperation(value = "게시글 상세 조회 API", httpMethod = "GET")
    @GetMapping("/{articleId}/applyArticle/{uuid}")
    public DataResDto<?> searchApply(@PathVariable("articleId") Integer articleId, @PathVariable("uuid") UUID uuid) {
        return articleService.searchApply(articleId, uuid);
    }
}
