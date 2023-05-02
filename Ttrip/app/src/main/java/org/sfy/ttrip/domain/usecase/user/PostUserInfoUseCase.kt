package org.sfy.ttrip.domain.usecase.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.sfy.ttrip.domain.repository.user.UserRepository
import javax.inject.Inject

class PostUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        nickName: String,
        intro: String,
        gender: String,
        profileFile: MultipartBody.Part?,
        markerFile: MultipartBody.Part?,
        age: String,
        fcmToken: String
    ) = withContext(Dispatchers.IO) {
        userRepository.postUserInfo(
            nickName, intro, gender, profileFile, markerFile, age, fcmToken
        )
    }
}