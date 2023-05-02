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

}