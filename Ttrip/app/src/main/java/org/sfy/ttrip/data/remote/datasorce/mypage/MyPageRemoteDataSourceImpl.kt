package org.sfy.ttrip.data.remote.datasorce.mypage

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.sfy.ttrip.data.remote.datasorce.board.BoardBriefResponse
import org.sfy.ttrip.data.remote.datasorce.user.UserInfoTestRequest
import org.sfy.ttrip.data.remote.service.CertificateApiService
import org.sfy.ttrip.data.remote.service.MyPageApiService
import javax.inject.Inject

class MyPageRemoteDataSourceImpl @Inject constructor(
    private val myPageApiService: MyPageApiService,
    private val certificateApiService: CertificateApiService
) : MyPageRemoteDataSource {

    override suspend fun updateUserInfo(body: UpdateUserInfoRequest) {
        myPageApiService.updateUserInfo(body)
    }

    override suspend fun updatePreferences(body: UserInfoTestRequest) {
        myPageApiService.updatePreferences(body)
    }

    override suspend fun getUserProfile(): UserProfileResponse =
        myPageApiService.getUserProfile().data!!

    override suspend fun logout() =
        myPageApiService.logout()

    override suspend fun updateBackgroundImg(backgroundImg: MultipartBody.Part?): BackgroundImgResponse =
        myPageApiService.updateBackgroundImg(backgroundImg).data!!

    override suspend fun updateProfileImg(
        markerImg: MultipartBody.Part?,
        profileImg: MultipartBody.Part?
    ) {
        myPageApiService.updateProfileImg(markerImg, profileImg)
    }

    override suspend fun getMyPosts(): List<BoardBriefResponse> =
        myPageApiService.getMyPosts().data!!

    override suspend fun certificateProfile(
        trainImg1: MultipartBody.Part?,
        trainImg2: MultipartBody.Part?,
        trainImg3: MultipartBody.Part?,
        testImg: String,
        nickname: String
    ): String {
        val map = mutableMapOf<String, @JvmSuppressWildcards RequestBody>()
        map["profileImgPath"] = testImg.toRequestBody("text/plain".toMediaTypeOrNull())
        map["nickname"] = nickname.toRequestBody("text/plain".toMediaTypeOrNull())
        return certificateApiService.certificateProfile(trainImg1, trainImg2, trainImg3, map).data!!
    }
}