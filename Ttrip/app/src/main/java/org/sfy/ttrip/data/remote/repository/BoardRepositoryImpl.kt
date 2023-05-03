package org.sfy.ttrip.data.remote.repository

import org.sfy.ttrip.common.util.wrapToResource
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.board.BoardRemoteDataSource
import org.sfy.ttrip.data.remote.datasorce.board.PostBoardRequest
import org.sfy.ttrip.data.remote.datasorce.board.SearchBoardRequest
import org.sfy.ttrip.domain.entity.board.BoardBrief
import org.sfy.ttrip.domain.entity.board.BoardDetail
import org.sfy.ttrip.domain.repository.board.BoardRepository
import java.time.LocalDateTime
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val boardRemoteDataSource: BoardRemoteDataSource
) : BoardRepository {

    override suspend fun postBoard(
        content: String,
        nation: String,
        city: String,
        startDate: LocalDateTime,
        endDateTime: LocalDateTime
    ) {
        boardRemoteDataSource.postBoard(
            PostBoardRequest(
                content,
                nation,
                city,
                startDate,
                endDateTime
            )
        )
    }

    override suspend fun getBoardList(
        condition: Int,
        nation: String,
        city: String,
        keyword: String
    ): Resource<List<BoardBrief>> =
        wrapToResource {
            boardRemoteDataSource.getBoardList(SearchBoardRequest(city, condition, keyword, nation))
                .map { it.toDomainModel() }
        }

    override suspend fun getBoardDetail(boardId: Int): Resource<BoardDetail> =
        wrapToResource {
            boardRemoteDataSource.getBoardDetail(boardId).toDomainModel()
        }

    override suspend fun deleteBoard(boardId: Int) {
        boardRemoteDataSource.deleteBoard(boardId)
    }

    override suspend fun finishBoard(boardId: Int) {
        boardRemoteDataSource.finishBoard(boardId)
    }
}