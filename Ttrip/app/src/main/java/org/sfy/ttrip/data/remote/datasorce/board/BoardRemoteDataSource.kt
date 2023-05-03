package org.sfy.ttrip.data.remote.datasorce.board


interface BoardRemoteDataSource {

    suspend fun postBoard(body: PostBoardRequest)

    suspend fun getBoardList(body: SearchBoardRequest): List<BoardBriefResponse>

    suspend fun getBoardDetail(boardId: Int): BoardDetailResponse
}