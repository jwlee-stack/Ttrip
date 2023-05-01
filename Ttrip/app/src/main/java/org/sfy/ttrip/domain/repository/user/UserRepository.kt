package org.sfy.ttrip.domain.repository.user

import okhttp3.MultipartBody
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.repository.CheckDuplicationResponse

interface UserRepository {

    suspend fun patchUserInfo(
        nickName: String,
        intro: String,
        gender: String,
        profileFile: MultipartBody.Part?,
        markerFile: MultipartBody.Part?,
        age: String,
        fcmToken: String
    )

    suspend fun checkDuplication(nickName: String): Resource<CheckDuplicationResponse>
}