package org.sfy.ttrip.domain.usecase.board

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.domain.repository.board.BoardRepository
import javax.inject.Inject

class FinishBoardUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(boardId: Int) {
        withContext(Dispatchers.IO) {
            boardRepository.finishBoard(boardId)
        }
    }
}