package org.sfy.ttrip.domain.usecase.board

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.board.PostBoard
import org.sfy.ttrip.domain.repository.board.BoardRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostBoardUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(
        city: String,
        content: String,
        endDate: String,
        nation: String,
        startDate: String,
        title: String
    ): Resource<PostBoard> = withContext(Dispatchers.IO) {
        boardRepository.postBoard(content, nation, city, startDate, endDate, title)
    }
}