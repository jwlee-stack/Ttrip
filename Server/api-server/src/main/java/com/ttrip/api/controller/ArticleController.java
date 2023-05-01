package com.ttrip.api.controller;

import com.ttrip.api.dto.ApplyReqDto;
import com.ttrip.api.dto.DataResDto;
import com.ttrip.api.dto.NewArticleReqDto;
import com.ttrip.api.dto.SearchReqDto;
import com.ttrip.api.service.ArticleService;
import com.ttrip.api.service.impl.MemberDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;
//  dto쓸려고 post요청
    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 검색 성공"),
            @ApiResponse(code = 400, message = "게시글 검색 실패")
    })
    @ApiOperation(value = "게시글 목록 조회 API", httpMethod = "POST")
    @PostMapping
    public DataResDto<?> search(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody SearchReqDto searchReqDto) {
        return articleService.search(searchReqDto);
    }
    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 상세 조회 성공"),
            @ApiResponse(code = 400, message = "게시글 상세 조회 실패")
    })
    @ApiOperation(value = "게시글 상세 조회 API", httpMethod = "GET")
    @GetMapping
    public DataResDto<?> getAll(@AuthenticationPrincipal MemberDetails memberDetails) {
        return articleService.getAll();
    }
    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 생성 성공"),
            @ApiResponse(code = 400, message = "게시글 생성 실패")
    })
    @ApiOperation(value = "게시글 생성 API", httpMethod = "POST")
    @PostMapping("/new")
    public DataResDto<?> newArticle(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody NewArticleReqDto newArticleReqDto) {
        return articleService.newArticle(newArticleReqDto, memberDetails.getMember().getMemberUuid());
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 상세 조회 성공"),
            @ApiResponse(code = 400, message = "게시글 상세 조회 실패")
    })
    @ApiOperation(value = "게시글 상세 조회 API", httpMethod = "GET")
    @GetMapping("/{articleId}")
    public DataResDto<?> searchDetail(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable("articleId") Integer articleId) {
        return articleService.searchDetail(articleId, memberDetails.getMember().getMemberUuid());
    }
    @ApiResponses({
            @ApiResponse(code = 200, message = "게시글 삭제 성공"),
            @ApiResponse(code = 400, message = "게시글 삭제 실패")
    })
    @ApiOperation(value = "게시글 삭제 API", httpMethod = "Delete")
    @DeleteMapping("/{articleId}")
    public DataResDto<?> eraseArticle(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable("articleId") Integer articleId) {
        return articleService.eraseArticle(articleId, memberDetails.getMember().getMemberUuid());
    }
    @ApiResponses({
            @ApiResponse(code = 200, message = "신청이 완료되었습니다."),
            @ApiResponse(code = 400, message = "매칭 참여 신청 실패")
    })
    @ApiOperation(value = "매칭 참여 신청 API", httpMethod = "POST")
    @PostMapping("/newApply")
    public DataResDto<?> newApply(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody ApplyReqDto applyReqDto) {
        return articleService.newApply(applyReqDto, memberDetails.getMember().getMemberUuid());
    }
    @ApiResponses({
            @ApiResponse(code = 200, message = "신청한 유저 목록이 조회되었습니다"),
            @ApiResponse(code = 400, message = "신청한 유저 목록이 조회 실패")
    })
    @ApiOperation(value = "게시글 상세 조회 API", httpMethod = "GET")
    @GetMapping("/{articleId}/applyArticle/")
    public DataResDto<?> searchApply(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable("articleId") Integer articleId) {
        return articleService.searchApply(articleId, memberDetails.getMember().getMemberUuid());
    }
    @ApiResponses({
            @ApiResponse(code = 200, message = "모집이 종료되었습니다."),
            @ApiResponse(code = 400, message = "모집 종료 실패")
    })
    @ApiOperation(value = "모집 종료 신청 API", httpMethod = "POST")
    @PostMapping("/{articleId}/end")
    public DataResDto<?> endArticle(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable("articleId") Integer articleId) {
        return articleService.endArticle(articleId, memberDetails.getMember().getMemberUuid());
    }
}
