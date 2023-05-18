package org.sfy.ttrip.domain.usecase.board

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.domain.repository.board.BoardRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteBoardUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(boardId: Int) {
        withContext(Dispatchers.IO) {
            boardRepository.deleteBoard(boardId)
        }
    }
}