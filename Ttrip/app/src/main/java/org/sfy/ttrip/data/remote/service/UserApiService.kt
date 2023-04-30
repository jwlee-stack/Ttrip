package org.sfy.ttrip.data.remote.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.PartMap

interface UserApiService {
    @Multipart
    @PATCH("/api/members/update")
    suspend fun patchUserInfo(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part profileFile: MultipartBody.Part?,
        @Part markerFile: MultipartBody.Part?
    )
}