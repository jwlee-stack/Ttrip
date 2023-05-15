package org.sfy.ttrip.data.remote.repository

import okhttp3.MultipartBody
import org.sfy.ttrip.common.util.wrapToResource
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.mypage.MyPageRemoteDataSource
import org.sfy.ttrip.data.remote.datasorce.mypage.UpdateUserInfoRequest
import org.sfy.ttrip.data.remote.datasorce.user.UserInfoTestRequest
import org.sfy.ttrip.domain.entity.board.BoardBrief
import org.sfy.ttrip.domain.entity.mypage.BackgroundImg
import org.sfy.ttrip.domain.entity.mypage.UserProfile
import org.sfy.ttrip.domain.repository.mypage.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageRemoteDataSource: MyPageRemoteDataSource
) : MyPageRepository {

    override suspend fun updateUserInfo(age: Int, gender: String, intro: String, nickname: String) {
        myPageRemoteDataSource.updateUserInfo(UpdateUserInfoRequest(age, gender, intro, nickname))
    }

    override suspend fun updatePreferences(
        preferCheapHotelThanComfort: Int,
        preferCheapTraffic: Int,
        preferDirectFlight: Int,
        preferGoodFood: Int,
        preferNatureThanCity: Int,
        preferPersonalBudget: Int,
        preferPlan: Int,
        preferShoppingThanTour: Int,
        preferTightSchedule: Int
    ) {
        myPageRemoteDataSource.updatePreferences(
            UserInfoTestRequest(
                preferCheapHotelThanComfort,
                preferCheapTraffic,
                preferDirectFlight,
                preferGoodFood,
                preferNatureThanCity,
                preferPersonalBudget,
                preferPlan,
                preferShoppingThanTour,
                preferTightSchedule
            )
        )
    }

    override suspend fun getUserProfile(): Resource<UserProfile> = wrapToResource {
        myPageRemoteDataSource.getUserProfile().toDomainModel()
    }

    override suspend fun logout() =
        myPageRemoteDataSource.logout()

    override suspend fun updateBackgroundImg(backgroundImg: MultipartBody.Part?): Resource<BackgroundImg> =
        wrapToResource {
            myPageRemoteDataSource.updateBackgroundImg(backgroundImg).toDomainModel()
        }

    override suspend fun updateProfileImg(
        markerImg: MultipartBody.Part?,
        profileImg: MultipartBody.Part?
    ) = myPageRemoteDataSource.updateProfileImg(markerImg, profileImg)

    override suspend fun getMyPosts(): Resource<List<BoardBrief>> = wrapToResource {
        myPageRemoteDataSource.getMyPosts().map { it.toDomainModel() }
    }

    override suspend fun certificateProfile(
        trainImg1: MultipartBody.Part?,
        trainImg2: MultipartBody.Part?,
        trainImg3: MultipartBody.Part?,
        testImg: String,
        nickname: String
    ): String =
        myPageRemoteDataSource.certificateProfile(
            trainImg1,
            trainImg2,
            trainImg3,
            testImg,
            nickname
        )
}