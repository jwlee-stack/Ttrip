package org.sfy.ttrip.data.remote.repository

import org.sfy.ttrip.data.remote.datasorce.mypage.MyPageRemoteDataSource
import org.sfy.ttrip.data.remote.datasorce.mypage.UpdateUserInfoRequest
import org.sfy.ttrip.domain.repository.mypage.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageRemoteDataSource: MyPageRemoteDataSource
) : MyPageRepository {

    override suspend fun updateUserInfo(age: Int, gender: String, intro: String, nickname: String) {
        myPageRemoteDataSource.updateUserInfo(UpdateUserInfoRequest(age, gender, intro, nickname))
    }
}