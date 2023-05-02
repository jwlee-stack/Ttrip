package org.sfy.ttrip.domain.usecase.mypage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.domain.repository.mypage.MyPageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateUserInfoUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(age: Int, gender: String, intro: String, nickname: String) {
        withContext(Dispatchers.IO) {
            myPageRepository.updateUserInfo(age, gender, intro, nickname)
        }
    }
}