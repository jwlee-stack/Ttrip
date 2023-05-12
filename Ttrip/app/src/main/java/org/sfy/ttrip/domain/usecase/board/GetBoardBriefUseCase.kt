package org.sfy.ttrip.domain.usecase.board

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.board.BoardBrief
import org.sfy.ttrip.domain.repository.board.BoardRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetBoardBriefUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(
        condition: Int,
        nation: String,
        city: String,
        keyword: String
    ): Resource<List<BoardBrief>> =
        withContext(Dispatchers.IO) {
            boardRepository.getBoardList(condition, nation, city, keyword)
        }
}