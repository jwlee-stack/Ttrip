package org.sfy.ttrip.data.remote.service

import okhttp3.MultipartBody
import org.sfy.ttrip.data.remote.datasorce.base.BaseResponse
import org.sfy.ttrip.data.remote.datasorce.mypage.BackgroundImgResponse
import org.sfy.ttrip.data.remote.datasorce.mypage.UpdateUserInfoRequest
import org.sfy.ttrip.data.remote.datasorce.mypage.UserProfileResponse
import org.sfy.ttrip.data.remote.datasorce.user.UserInfoTestRequest
import retrofit2.http.*

interface MyPageApiService {

    @PATCH("/api/mypage/update")
    suspend fun updateUserInfo(@Body body: UpdateUserInfoRequest)

    @PATCH("/api/mypage/update/preferences")
    suspend fun updatePreferences(@Body body: UserInfoTestRequest)

    @GET("/api/mypage/view/myInfo")
    suspend fun getUserProfile(): BaseResponse<UserProfileResponse>

    @DELETE("/api/members/logout")
    suspend fun logout()

    @Multipart
    @PATCH("/api/mypage/update/background")
    suspend fun updateBackgroundImg(
        @Part backgroundImg: MultipartBody.Part?
    ): BaseResponse<BackgroundImgResponse>
}