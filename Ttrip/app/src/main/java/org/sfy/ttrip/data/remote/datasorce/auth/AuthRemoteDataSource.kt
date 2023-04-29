package org.sfy.ttrip.data.remote.datasorce.auth

interface AuthRemoteDataSource {

    suspend fun requestSignUp(body: AuthRequest)

    suspend fun requestLogin(body: AuthRequest): AuthResponse
}