package org.sfy.ttrip.data.remote.service

import org.sfy.ttrip.data.remote.datasorce.auth.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/members")
    suspend fun requestSignUp(@Body body: SignUpRequest)
}