package org.sfy.ttrip.data.remote.datasorce.mypage

import org.sfy.ttrip.data.remote.datasorce.user.UserInfoTestRequest

interface MyPageRemoteDataSource {

    suspend fun updateUserInfo(body: UpdateUserInfoRequest)

    suspend fun updatePreferences(body: UserInfoTestRequest)
}