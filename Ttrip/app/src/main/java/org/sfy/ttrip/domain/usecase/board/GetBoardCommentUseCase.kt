package org.sfy.ttrip.domain.usecase.board

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.board.BoardComment
import org.sfy.ttrip.domain.repository.board.BoardRepository

class GetBoardCommentUseCase(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(boardId: Int): Resource<List<BoardComment>> =
        withContext(Dispatchers.IO) {
            boardRepository.getBoardComment(boardId)
        }
}