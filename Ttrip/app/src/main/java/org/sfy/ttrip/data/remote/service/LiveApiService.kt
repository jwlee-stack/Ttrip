package org.sfy.ttrip.data.remote.service

import org.sfy.ttrip.data.remote.datasorce.live.LiveUserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LiveApiService {

    @GET("/api/live/{city}/{lng}/{lat}")
    suspend fun getLiveUsers(
        @Path("city") city: String,
        @Path("lng") lng: Double,
        @Path("lat") lat: Double
    ): List<LiveUserResponse>
}