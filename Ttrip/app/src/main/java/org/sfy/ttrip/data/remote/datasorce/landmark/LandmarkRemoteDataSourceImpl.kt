package org.sfy.ttrip.data.remote.datasorce.landmark

import org.sfy.ttrip.data.remote.service.LandmarkApiService
import javax.inject.Inject

class LandmarkRemoteDataSourceImpl @Inject constructor(
    private val landmarkApiService: LandmarkApiService
) : LandmarkRemoteDataSource {

    override suspend fun getLandmarks(): List<LandmarkResponse> =
        landmarkApiService.getLandmarks().data!!
}