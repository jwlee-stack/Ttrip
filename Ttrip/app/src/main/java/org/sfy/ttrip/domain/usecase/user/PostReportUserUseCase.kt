package org.sfy.ttrip.domain.usecase.user

import org.sfy.ttrip.domain.repository.user.UserRepository
import javax.inject.Inject

class PostReportUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(reportContext: String, reportedNickname: String) {
        userRepository.postReportUser(reportContext, reportedNickname)
    }
}