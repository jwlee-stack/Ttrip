package org.sfy.ttrip.domain.repository.mypage

import okhttp3.MultipartBody
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.mypage.CertificateProfileResponse
import org.sfy.ttrip.domain.entity.board.BoardBrief
import org.sfy.ttrip.domain.entity.mypage.BackgroundImg
import org.sfy.ttrip.domain.entity.mypage.UserProfile

interface MyPageRepository {

    suspend fun updateUserInfo(age: Int, gender: String, intro: String, nickname: String)

    suspend fun updatePreferences(
        preferCheapHotelThanComfort: Int,
        preferCheapTraffic: Int,
        preferDirectFlight: Int,
        preferGoodFood: Int,
        preferNatureThanCity: Int,
        preferPersonalBudget: Int,
        preferPlan: Int,
        preferShoppingThanTour: Int,
        preferTightSchedule: Int
    )

    suspend fun getUserProfile(): Resource<UserProfile>

    suspend fun logout()

    suspend fun updateBackgroundImg(
        backgroundImg: MultipartBody.Part?
    ): Resource<BackgroundImg>

    suspend fun updateProfileImg(
        markerImg: MultipartBody.Part?,
        profileImg: MultipartBody.Part?
    )

    suspend fun getMyPosts(): Resource<List<BoardBrief>>

    suspend fun certificateProfile(
        trainImg1: MultipartBody.Part?,
        trainImg2: MultipartBody.Part?,
        trainImg3: MultipartBody.Part?,
        testImg: String,
        nickname: String
    ): CertificateProfileResponse
}