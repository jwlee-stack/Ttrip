package org.sfy.ttrip.data.remote.service

import org.sfy.ttrip.data.remote.datasorce.base.BaseResponse
import org.sfy.ttrip.data.remote.datasorce.landmark.BadgeResponse
import org.sfy.ttrip.data.remote.datasorce.landmark.LandmarkResponse
import retrofit2.http.GET

interface LandmarkApiService {

    @GET("/api/landmarks")
    suspend fun getLandmarks(): BaseResponse<List<LandmarkResponse>>

    @GET("/api/landmarks/badges")
    suspend fun getBadges(): BaseResponse<List<BadgeResponse>>
}