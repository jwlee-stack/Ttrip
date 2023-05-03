package org.sfy.ttrip.data.remote.datasorce.live

import org.sfy.ttrip.data.remote.service.LiveApiService
import javax.inject.Inject

class LiveRemoteDataSourceImpl @Inject constructor(
    private val liveApiService: LiveApiService
) : LiveRemoteDataSource {

    override suspend fun getLiveUsers(
        city: String,
        lng: Double,
        lat: Double
    ): List<LiveUserResponse?> =
        liveApiService.getLiveUsers(city, lng, lat).data!!

    override suspend fun createSession(): SessionResponse =
        liveApiService.createSession().data!!
}