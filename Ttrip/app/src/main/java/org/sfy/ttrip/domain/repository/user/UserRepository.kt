package org.sfy.ttrip.domain.repository.user

import okhttp3.MultipartBody
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.user.CheckDuplicationResponse
import org.sfy.ttrip.data.remote.datasorce.user.UserInfoTestRequest
import org.sfy.ttrip.domain.entity.user.UserProfileDialog

interface UserRepository {

    suspend fun postUserInfo(
        nickName: String,
        intro: String,
        gender: String,
        profileFile: MultipartBody.Part?,
        markerFile: MultipartBody.Part?,
        age: String,
        fcmToken: String
    ): Resource<UserProfileDialog>

    suspend fun checkDuplication(nickName: String): Resource<CheckDuplicationResponse>

    suspend fun postUserInfoTest(userTest: UserInfoTestRequest)

    suspend fun getUserProfile(nickName: String): Resource<UserProfileDialog>

    suspend fun postUserFcm(fcmToken: String)

    suspend fun postEvaluateUser(matchHistoryId: String, rate: Int)

    suspend fun postReportUser(reportContext: String, reportedNickname: String)
}