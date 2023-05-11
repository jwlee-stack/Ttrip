package org.sfy.ttrip.data.remote.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.sfy.ttrip.data.remote.datasorce.base.BaseResponse
import org.sfy.ttrip.data.remote.datasorce.user.CheckDuplicationResponse
import org.sfy.ttrip.data.remote.datasorce.user.UserInfoTestRequest
import org.sfy.ttrip.data.remote.datasorce.user.UserProfileDialogResponse
import retrofit2.http.*

interface UserApiService {
    @Multipart
    @PATCH("/api/members/setInfo")
    suspend fun patchUserInfo(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part profileImg: MultipartBody.Part?,
        @Part markerImg: MultipartBody.Part?
    ): BaseResponse<UserProfileDialogResponse>

    @GET("/api/members/{nickname}/exists")
    suspend fun checkDuplication(@Path("nickname") nickname: String): BaseResponse<CheckDuplicationResponse>

    @PATCH("/api/members/preferences")
    suspend fun patchUserInfoTest(@Body body: UserInfoTestRequest)

    @GET("/api/members/{nickname}")
    suspend fun getUserProfile(@Path("nickname") nickname: String): BaseResponse<UserProfileDialogResponse>
}