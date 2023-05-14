package org.sfy.ttrip.data.remote.datasorce.auth

import org.sfy.ttrip.data.remote.service.AuthApiService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRemoteDataSource {

    override suspend fun requestSignUp(body: AuthRequest) {
        authApiService.requestSignUp(body)
    }

    override suspend fun requestLogin(body: AuthRequest): AuthResponse =
        authApiService.requestLogin(body).data!!

    override suspend fun requestAccessToken(body: AccessTokenRequest): AccessTokenResponse =
        authApiService.requestAccessToken(body).data!!
}