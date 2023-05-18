package org.sfy.ttrip.data.remote.repository

import org.sfy.ttrip.common.util.wrapToResource
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.board.*
import org.sfy.ttrip.domain.entity.board.*
import org.sfy.ttrip.domain.repository.board.BoardRepository
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val boardRemoteDataSource: BoardRemoteDataSource
) : BoardRepository {

    override suspend fun postBoard(
        content: String,
        nation: String,
        city: String,
        startDate: String,
        endDateTime: String,
        title: String
    ): Resource<PostBoard> =
        wrapToResource {
            boardRemoteDataSource.postBoard(
                PostBoardRequest(
                    content,
                    nation,
                    city,
                    startDate,
                    endDateTime,
                    title
                )
            ).toDomainModel()
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

    override suspend fun getBoardComment(boardId: Int): Resource<List<BoardComment>> =
        wrapToResource {
            boardRemoteDataSource.getBoardComment(boardId).map { it.toDomainModel() }
        }

    override suspend fun postComment(boardId: Int, comment: String) {
        boardRemoteDataSource.postComment(CommentRequest(boardId, comment))
    }

    override suspend fun getRecommendBoard(
        boardId: Int,
        authorId: Int,
        city: String,
        content: String,
        numOfArticles: Int
    ): Resource<List<RecommendBoard>> =
        wrapToResource {
            boardRemoteDataSource.postRecommendBoard(
                RecommendBoardRequest(
                    boardId,
                    authorId,
                    city,
                    content,
                    numOfArticles
                )
            ).map { it.toDomainModel() }
        }
}