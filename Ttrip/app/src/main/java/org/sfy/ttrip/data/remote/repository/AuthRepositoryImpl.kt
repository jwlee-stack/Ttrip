package org.sfy.ttrip.data.remote.repository

import org.sfy.ttrip.data.remote.datasorce.auth.AuthRemoteDataSource
import org.sfy.ttrip.data.remote.datasorce.auth.AuthRequest
import org.sfy.ttrip.domain.entity.user.Auth
import org.sfy.ttrip.domain.repository.auth.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun requestSignUp(phoneNumber: String, password: String) {
        authRemoteDataSource.requestSignUp(AuthRequest(phoneNumber, phoneNumber))
    }

    override suspend fun requestLogin(phoneNumber: String, password: String): Auth =
        authRemoteDataSource.loginRequest(AuthRequest(phoneNumber, password)).toDomainModel()
}