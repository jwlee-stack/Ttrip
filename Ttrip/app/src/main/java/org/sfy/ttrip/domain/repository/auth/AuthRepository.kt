package org.sfy.ttrip.domain.repository.auth

import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.auth.AccessToken
import org.sfy.ttrip.domain.entity.auth.Auth


interface AuthRepository {

    suspend fun requestSignUp(phoneNumber: String, password: String)

    suspend fun requestLogin(phoneNumber: String, password: String): Resource<Auth>

    suspend fun requestAccessToken(accessToken: String, refreshToken: String): Resource<AccessToken>
}