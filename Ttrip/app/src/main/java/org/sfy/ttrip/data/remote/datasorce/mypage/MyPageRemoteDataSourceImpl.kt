package org.sfy.ttrip.data.remote.datasorce.mypage

import org.sfy.ttrip.data.remote.datasorce.user.UserInfoTestRequest
import org.sfy.ttrip.data.remote.service.MyPageApiService
import javax.inject.Inject

class MyPageRemoteDataSourceImpl @Inject constructor(
    private val myPageApiService: MyPageApiService
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
}