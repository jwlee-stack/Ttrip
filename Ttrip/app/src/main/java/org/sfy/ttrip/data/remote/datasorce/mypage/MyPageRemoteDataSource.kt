package org.sfy.ttrip.data.remote.datasorce.mypage

import okhttp3.MultipartBody
import org.sfy.ttrip.data.remote.datasorce.board.BoardBriefResponse
import org.sfy.ttrip.data.remote.datasorce.user.UserInfoTestRequest

interface MyPageRemoteDataSource {

    suspend fun updateUserInfo(body: UpdateUserInfoRequest)

    suspend fun updatePreferences(body: UserInfoTestRequest)

    suspend fun getUserProfile(): UserProfileResponse

    suspend fun logout()

    suspend fun updateBackgroundImg(
        backgroundImg: MultipartBody.Part?
    ): BackgroundImgResponse

    suspend fun updateProfileImg(
        markerImg: MultipartBody.Part?,
        profileImg: MultipartBody.Part?
    )

    suspend fun getMyPosts(): List<BoardBriefResponse>
}