package org.sfy.ttrip.domain.usecase.mypage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.sfy.ttrip.domain.repository.mypage.MyPageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateProfileImgUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        markerImg: MultipartBody.Part?,
        profileImg: MultipartBody.Part?
    ) = withContext(Dispatchers.IO) {
        myPageRepository.updateProfileImg(markerImg, profileImg)
    }
}