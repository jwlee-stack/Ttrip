package org.sfy.ttrip.domain.repository.board

import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.board.*

interface BoardRepository {

    suspend fun postBoard(
        content: String,
        nation: String,
        city: String,
        startDate: String,
        endDateTime: String,
        title: String
    ): Resource<PostBoard>

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

    suspend fun postComment(boardId: Int, comment: String)

    suspend fun getRecommendBoard(
        boardId: Int,
        authorId: Int,
        city: String,
        content: String,
        numOfArticles: Int
    ): Resource<List<RecommendBoard>>
}