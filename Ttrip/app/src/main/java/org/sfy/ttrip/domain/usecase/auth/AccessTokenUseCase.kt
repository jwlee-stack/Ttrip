package org.sfy.ttrip.domain.usecase.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.auth.AccessToken
import org.sfy.ttrip.domain.repository.auth.AuthRepository
import javax.inject.Inject

class AccessTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(accessToken: String, refreshToken: String): Resource<AccessToken> =
        withContext(Dispatchers.IO) {
            authRepository.requestAccessToken(accessToken, refreshToken)
        }
}