package org.sfy.ttrip.data.remote.repository

import okhttp3.MultipartBody
import org.sfy.ttrip.common.util.wrapToResource
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.user.CheckDuplicationResponse
import org.sfy.ttrip.data.remote.datasorce.user.UserInfoTestRequest
import org.sfy.ttrip.data.remote.datasorce.user.UserRemoteDataSource
import org.sfy.ttrip.domain.entity.user.UserProfileDialog
import org.sfy.ttrip.domain.repository.user.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun postUserInfo(
        nickName: String,
        intro: String,
        gender: String,
        profileFile: MultipartBody.Part?,
        markerFile: MultipartBody.Part?,
        age: String,
        fcmToken: String
    ): Resource<UserProfileDialog> =
        wrapToResource {
            userRemoteDataSource.postUserInfo(
                nickName,
                intro,
                gender,
                profileFile,
                markerFile,
                age,
                fcmToken
            ).toDomainModel()
        }

    override suspend fun checkDuplication(nickName: String): Resource<CheckDuplicationResponse> =
        wrapToResource {
            userRemoteDataSource.checkDuplication(nickName)
        }

    override suspend fun postUserInfoTest(userTest: UserInfoTestRequest) {
        wrapToResource {
            userRemoteDataSource.postUserInfoTest(userTest)
        }
    }

    override suspend fun getUserProfile(nickName: String): Resource<UserProfileDialog> =
        wrapToResource {
            userRemoteDataSource.getUserProfile(nickName).toDomainModel()
        }

    override suspend fun postUserFcm(fcmToken: String) {
        userRemoteDataSource.postUserFcm(fcmToken)
    }

    override suspend fun postEvaluateUser(matchHistoryId: String, rate: Int) {
        userRemoteDataSource.postEvaluateUser(matchHistoryId, rate)
    }
}