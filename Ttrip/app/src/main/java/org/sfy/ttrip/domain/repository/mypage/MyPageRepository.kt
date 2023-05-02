package org.sfy.ttrip.domain.repository.mypage

interface MyPageRepository {

    suspend fun updateUserInfo(age: Int, gender: String, intro: String, nickname: String)
}