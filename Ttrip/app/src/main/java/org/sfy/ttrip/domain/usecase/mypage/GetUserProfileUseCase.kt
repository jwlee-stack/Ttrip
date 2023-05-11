package org.sfy.ttrip.domain.usecase.mypage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.mypage.UserProfile
import org.sfy.ttrip.domain.repository.mypage.MyPageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserProfileUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Resource<UserProfile> =
        withContext(Dispatchers.IO) {
            myPageRepository.getUserProfile()
        }
}