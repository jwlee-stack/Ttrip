package org.sfy.ttrip.data.remote.datasorce.landmark

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.sfy.ttrip.data.remote.service.LandmarkApiService
import javax.inject.Inject

class LandmarkRemoteDataSourceImpl @Inject constructor(
    private val landmarkApiService: LandmarkApiService
) : LandmarkRemoteDataSource {

    override suspend fun getLandmarks(): List<LandmarkResponse> =
        landmarkApiService.getLandmarks().data!!

    override suspend fun createDoodle(
        positionX: Double,
        positionY: Double,
        positionZ: Double,
        doodleImgPath: MultipartBody.Part?,
        landmarkId: Int,
        latitude: Double,
        longitude: Double
    ) {
        val map = mutableMapOf<String, @JvmSuppressWildcards RequestBody>()
        map["positionX"] = positionX.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        map["positionY"] = positionY.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        map["positionZ"] = positionZ.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        map["landmarkId"] = landmarkId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        map["latitude"] = latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        map["longitude"] = longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        landmarkApiService.createDoodle(map, doodleImgPath)
    }

    override suspend fun getDoodles(landmarkId: Int): List<DoodleResponse> =
        landmarkApiService.getDoodles(landmarkId).data!!

    override suspend fun getBadges(): List<BadgeResponse> =
        landmarkApiService.getBadges().data!!
}