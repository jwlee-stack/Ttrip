package org.sfy.ttrip.domain.usecase.mypage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.board.BoardBrief
import org.sfy.ttrip.domain.repository.mypage.MyPageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMyPostsUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Resource<List<BoardBrief>> =
        withContext(Dispatchers.IO) {
            myPageRepository.getMyPosts()
        }
}