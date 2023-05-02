package org.sfy.ttrip.data.remote.datasorce.user

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.sfy.ttrip.data.remote.service.UserApiService
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRemoteDataSource {

    override suspend fun postUserInfo(
        nickName: String,
        intro: String,
        gender: String,
        profileFile: MultipartBody.Part?,
        markerFile: MultipartBody.Part?,
        age: String,
        fcmToken: String
    ) {
        val map = mutableMapOf<String, @JvmSuppressWildcards RequestBody>()
        map["nickname"] = nickName.toRequestBody("text/plain".toMediaTypeOrNull())
        map["intro"] = intro.toRequestBody("text/plain".toMediaTypeOrNull())
        map["gender"] = gender.toRequestBody("text/plain".toMediaTypeOrNull())
        map["age"] = age.toRequestBody("text/plain".toMediaTypeOrNull())
        map["fcmToken"] = fcmToken.toRequestBody("text/plain".toMediaTypeOrNull())
        return userApiService.patchUserInfo(map, profileFile, markerFile)
    }

    override suspend fun checkDuplication(nickName: String): CheckDuplicationResponse =
        userApiService.checkDuplication(nickName).data!!

    override suspend fun postUserInfoTest(body: UserInfoTestRequest) {
        userApiService.patchUserInfoTest(body)
    }
}