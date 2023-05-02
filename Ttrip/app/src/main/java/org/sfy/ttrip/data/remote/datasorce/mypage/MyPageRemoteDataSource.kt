package org.sfy.ttrip.data.remote.datasorce.mypage

interface MyPageRemoteDataSource {

    suspend fun updateUserInfo(body: UpdateUserInfoRequest)
}