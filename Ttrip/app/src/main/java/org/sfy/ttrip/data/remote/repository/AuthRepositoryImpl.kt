package org.sfy.ttrip.data.remote.repository

import org.sfy.ttrip.data.remote.datasorce.auth.AuthRemoteDataSource
import org.sfy.ttrip.data.remote.datasorce.auth.SignUpRequest
import org.sfy.ttrip.domain.repository.auth.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun requestSignUp(phoneNumber: String, password: String) {
        authRemoteDataSource.requestSignUp(SignUpRequest(phoneNumber, phoneNumber))
    }
}