package org.sfy.ttrip.domain.repository.user

import okhttp3.MultipartBody
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.user.CheckDuplicationResponse
import org.sfy.ttrip.data.remote.datasorce.user.UserInfoTestRequest

interface UserRepository {

    suspend fun postUserInfo(
        nickName: String,
        intro: String,
        gender: String,
        profileFile: MultipartBody.Part?,
        markerFile: MultipartBody.Part?,
        age: String,
        fcmToken: String
    )

    suspend fun checkDuplication(nickName: String): Resource<CheckDuplicationResponse>

    suspend fun postUserInfoTest(userTest: UserInfoTestRequest)
}