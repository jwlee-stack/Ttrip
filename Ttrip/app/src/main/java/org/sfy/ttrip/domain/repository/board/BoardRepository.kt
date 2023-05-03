package org.sfy.ttrip.domain.repository.board

import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.board.BoardBrief
import org.sfy.ttrip.domain.entity.board.BoardComment
import org.sfy.ttrip.domain.entity.board.BoardDetail
import java.time.LocalDateTime

interface BoardRepository {

    suspend fun postBoard(
        content: String,
        nation: String,
        city: String,
        startDate: LocalDateTime,
        endDateTime: LocalDateTime
    )

    suspend fun getBoardList(
        condition: Int,
        nation: String,
        city: String,
        keyword: String
    ): Resource<List<BoardBrief>>

    suspend fun getBoardDetail(boardId: Int): Resource<BoardDetail>

    suspend fun deleteBoard(boardId: Int)

    suspend fun finishBoard(boardId: Int)

    suspend fun getBoardComment(boardId: Int): Resource<List<BoardComment>>
}