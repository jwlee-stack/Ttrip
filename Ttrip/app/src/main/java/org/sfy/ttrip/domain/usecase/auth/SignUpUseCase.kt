package org.sfy.ttrip.domain.usecase.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.domain.repository.auth.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String, password: String) {
        withContext(Dispatchers.IO) {
            authRepository.requestSignUp(phoneNumber, password)
        }
    }
}