package org.sfy.ttrip.domain.usecase.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.repository.CheckDuplicationResponse
import org.sfy.ttrip.domain.repository.user.UserRepository
import javax.inject.Inject

class CheckDuplicationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(nickname: String): Resource<CheckDuplicationResponse> = withContext(Dispatchers.IO) {
        userRepository.checkDuplication(nickname)
    }
}