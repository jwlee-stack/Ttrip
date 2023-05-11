package org.sfy.ttrip.domain.usecase.board

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.board.BoardDetail
import org.sfy.ttrip.domain.repository.board.BoardRepository
import javax.inject.Inject

class GetBoardDetailUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(boardId: Int): Resource<BoardDetail> =
        withContext(Dispatchers.IO) {
            boardRepository.getBoardDetail(boardId)
        }
}