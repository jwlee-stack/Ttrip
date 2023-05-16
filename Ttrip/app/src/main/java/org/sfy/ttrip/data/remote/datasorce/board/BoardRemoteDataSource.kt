package org.sfy.ttrip.data.remote.datasorce.board


interface BoardRemoteDataSource {

    suspend fun postBoard(body: PostBoardRequest): PostBoardResponse

    suspend fun getBoardList(body: SearchBoardRequest): List<BoardBriefResponse>

    suspend fun getBoardDetail(boardId: Int): BoardDetailResponse

    suspend fun deleteBoard(boardId: Int)

    suspend fun finishBoard(boardId: Int)

    suspend fun getBoardComment(boardId: Int): List<BoardCommentResponse>

    suspend fun postComment(body: CommentRequest)

    suspend fun postRecommendBoard(body: RecommendBoardRequest): RecommendBoardResponse
}