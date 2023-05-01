package org.sfy.ttrip.data.remote.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.sfy.ttrip.data.remote.datasorce.base.BaseResponse
import org.sfy.ttrip.data.remote.datasorce.user.CheckDuplicationResponse
import retrofit2.http.*

interface UserApiService {
    @Multipart
    @PATCH("/api/members/update")
    suspend fun patchUserInfo(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part profileFile: MultipartBody.Part?,
        @Part markerFile: MultipartBody.Part?
    )

    @GET("/api/members/{nickname}/exists")
    suspend fun checkDuplication(@Path("nickname") nickname: String): BaseResponse<CheckDuplicationResponse>

    @PATCH(" /api/members/preferences")
    suspend fun patchUserTestInfo(

    )
}