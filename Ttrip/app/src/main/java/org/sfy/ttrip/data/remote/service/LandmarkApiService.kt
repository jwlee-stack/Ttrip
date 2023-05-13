package org.sfy.ttrip.data.remote.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.sfy.ttrip.data.remote.datasorce.base.BaseResponse
import org.sfy.ttrip.data.remote.datasorce.landmark.LandmarkResponse
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface LandmarkApiService {

    @GET("/api/landmarks")
    suspend fun getLandmarks(): BaseResponse<List<LandmarkResponse>>

    @Multipart
    @POST("/api/landmarks/doodles")
    suspend fun createDoodle(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part doodleImgPath: MultipartBody.Part?
    )
}