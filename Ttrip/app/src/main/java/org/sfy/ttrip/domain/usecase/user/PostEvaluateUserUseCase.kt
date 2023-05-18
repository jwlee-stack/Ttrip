package org.sfy.ttrip.domain.usecase.user

import org.sfy.ttrip.domain.repository.user.UserRepository
import javax.inject.Inject

class PostEvaluateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(matchHistoryId: String, rate: Int) {
        userRepository.postEvaluateUser(matchHistoryId, rate)
    }
}