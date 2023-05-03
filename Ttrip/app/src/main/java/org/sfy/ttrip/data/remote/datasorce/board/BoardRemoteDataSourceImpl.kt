package org.sfy.ttrip.data.remote.datasorce.board

import org.sfy.ttrip.data.remote.service.BoardApiService
import javax.inject.Inject

class BoardRemoteDataSourceImpl @Inject constructor(
    private val boardApiService: BoardApiService
) : BoardRemoteDataSource {

    override suspend fun postBoard(body: PostBoardRequest) {
        boardApiService.postBoard(body)
    }

    override suspend fun getBoardList(body: SearchBoardRequest): List<BoardBriefResponse> =
        boardApiService.getBoardList(body).data!!

    override suspend fun getBoardDetail(boardId: Int): BoardDetailResponse =
        boardApiService.getBoardDetail(boardId).data!!

    override suspend fun deleteBoard(boardId: Int) {
        boardApiService.deleteBoard(boardId)
    }

    override suspend fun finishBoard(boardId: Int) {
        boardApiService.finishBoard(boardId)
    }

    override suspend fun getBoardComment(boardId: Int): List<BoardCommentResponse> =
        boardApiService.getBoardComment(boardId).data!!
}