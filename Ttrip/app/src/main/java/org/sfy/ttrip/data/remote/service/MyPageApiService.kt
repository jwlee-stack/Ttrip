package org.sfy.ttrip.data.remote.service

import org.sfy.ttrip.data.remote.datasorce.mypage.UpdateUserInfoRequest
import retrofit2.http.Body
import retrofit2.http.PATCH

interface MyPageApiService {

    @PATCH("/api/mypage/update")
    suspend fun updateUserInfo(@Body body: UpdateUserInfoRequest)
}