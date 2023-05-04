package org.sfy.ttrip.domain.usecase.board

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.domain.repository.board.BoardRepository
import javax.inject.Inject

class PostCommentUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(boardId: Int, comment: String) {
        withContext(Dispatchers.IO) {
            boardRepository.postComment(boardId, comment)
        }
    }
}