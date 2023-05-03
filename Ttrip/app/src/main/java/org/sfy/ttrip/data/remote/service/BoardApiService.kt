package org.sfy.ttrip.data.remote.service

import org.sfy.ttrip.data.remote.datasorce.base.BaseResponse
import org.sfy.ttrip.data.remote.datasorce.board.BoardBriefResponse
import org.sfy.ttrip.data.remote.datasorce.board.BoardDetailResponse
import org.sfy.ttrip.data.remote.datasorce.board.PostBoardRequest
import org.sfy.ttrip.data.remote.datasorce.board.SearchBoardRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BoardApiService {

    @POST("/api/articles/new")
    suspend fun postBoard(@Body body: PostBoardRequest)

    @POST("/api/articles")
    suspend fun getBoardList(@Body body: SearchBoardRequest): BaseResponse<List<BoardBriefResponse>>

    @GET("/api/articles/{articleId}")
    suspend fun getBoardDetail(@Path("articleId") articleId: Int): BaseResponse<BoardDetailResponse>

    @POST("/api/articles/{articleId}/end")
    suspend fun endBoard(@Path("articleId") articleId: Int)

    @DELETE("/api/articles/{articleId}")
    suspend fun deleteBoard(@Path("articleId") articleId: Int)
}