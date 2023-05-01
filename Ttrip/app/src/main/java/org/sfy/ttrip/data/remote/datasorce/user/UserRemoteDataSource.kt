package org.sfy.ttrip.data.remote.datasorce.user

import okhttp3.MultipartBody
import org.sfy.ttrip.data.remote.repository.CheckDuplicationResponse

interface UserRemoteDataSource {

    suspend fun patchUserInfo(
        nickName: String,
        intro: String,
        gender: String,
        profileFile: MultipartBody.Part?,
        markerFile: MultipartBody.Part?,
        age: String,
        fcmToken: String
    )

    suspend fun checkDuplication(nickName: String): CheckDuplicationResponse
}