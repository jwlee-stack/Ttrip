package org.sfy.ttrip.domain.usecase.board

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.board.PostBoard
import org.sfy.ttrip.domain.entity.board.RecommendBoard
import org.sfy.ttrip.domain.repository.board.BoardRepository
import javax.inject.Inject

class GetRecommendUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(postBoard: PostBoard): Resource<List<RecommendBoard>> =
        withContext(Dispatchers.IO) {
            boardRepository.getRecommendBoard(
                postBoard.articleId,
                postBoard.authorId,
                postBoard.city,
                postBoard.content!!,
                3
            )
        }
}
