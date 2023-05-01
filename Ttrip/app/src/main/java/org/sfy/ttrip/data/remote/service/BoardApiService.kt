package org.sfy.ttrip.data.remote.service

import org.sfy.ttrip.data.remote.datasorce.board.BoardDetailResponse
import org.sfy.ttrip.data.remote.datasorce.board.PostBoardRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BoardApiService {

    @POST("/api/articles/new")
    suspend fun postBoard(@Body body: PostBoardRequest)

    @GET("/api/articles/{articleId}")
    suspend fun getBoardDetail(@Path("articleId") articleId: Int): BoardDetailResponse

    @POST("/api/articles/{articleId}/end")
    suspend fun endBoard()
}