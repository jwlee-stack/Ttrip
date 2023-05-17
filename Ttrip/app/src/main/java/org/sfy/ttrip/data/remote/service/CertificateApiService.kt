package org.sfy.ttrip.data.remote.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.sfy.ttrip.data.remote.datasorce.mypage.CertificateProfileResponse
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface CertificateApiService {

    @Multipart
    @POST("/face")
    suspend fun certificateProfile(
        @Part trainImg1: MultipartBody.Part?,
        @Part trainImg2: MultipartBody.Part?,
        @Part trainImg3: MultipartBody.Part?,
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
    ): CertificateProfileResponse
}