package org.sfy.ttrip.domain.usecase.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.user.UserProfileDialog
import org.sfy.ttrip.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserProfileDialogUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(nickName: String): Resource<UserProfileDialog> =
        withContext(Dispatchers.IO) {
            userRepository.getUserProfile(nickName)
        }
}