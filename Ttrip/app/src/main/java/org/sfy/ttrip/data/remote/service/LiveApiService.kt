package org.sfy.ttrip.data.remote.service

import org.sfy.ttrip.data.remote.datasorce.base.BaseResponse
import org.sfy.ttrip.data.remote.datasorce.live.CallTokenRequest
import org.sfy.ttrip.data.remote.datasorce.live.CallTokenResponse
import org.sfy.ttrip.data.remote.datasorce.live.LiveUserResponse
import org.sfy.ttrip.data.remote.datasorce.live.SessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LiveApiService {

    @GET("/api/live/{city}/{lng}/{lat}")
    suspend fun getLiveUsers(
        @Path("city") city: String,
        @Path("lng") lng: Double,
        @Path("lat") lat: Double
    ): BaseResponse<List<LiveUserResponse>>

    @POST("/api/sessions")
    suspend fun createSession(): BaseResponse<SessionResponse>

    @POST("/api/sessions/{sessionId}/connections")
    suspend fun getCallToken(
        @Path("sessionId") sessionId: String,
        @Body body: CallTokenRequest
    ): BaseResponse<CallTokenResponse>
}