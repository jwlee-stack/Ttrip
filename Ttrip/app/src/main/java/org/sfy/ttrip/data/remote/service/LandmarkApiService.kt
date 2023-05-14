package org.sfy.ttrip.data.remote.service

import org.sfy.ttrip.data.remote.datasorce.base.BaseResponse
import org.sfy.ttrip.data.remote.datasorce.landmark.BadgeRequest
import org.sfy.ttrip.data.remote.datasorce.landmark.BadgeResponse
import org.sfy.ttrip.data.remote.datasorce.landmark.LandmarkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LandmarkApiService {

    @GET("/api/landmarks")
    suspend fun getLandmarks(): BaseResponse<List<LandmarkResponse>>

    @GET("/api/landmarks/badges")
    suspend fun getBadges(): BaseResponse<List<BadgeResponse>>

    @POST("/api/landmarks/badges")
    suspend fun issueBadge(@Body body: BadgeRequest): BaseResponse<BadgeResponse>
}