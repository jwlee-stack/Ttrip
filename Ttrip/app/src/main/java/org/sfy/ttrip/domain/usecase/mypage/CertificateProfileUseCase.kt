package org.sfy.ttrip.domain.usecase.mypage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.sfy.ttrip.domain.repository.mypage.MyPageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CertificateProfileUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        trainImg1: MultipartBody.Part?,
        trainImg2: MultipartBody.Part?,
        trainImg3: MultipartBody.Part?,
        testImg: String,
        nickname: String
    ): String = withContext(Dispatchers.IO) {
        myPageRepository.certificateProfile(
            trainImg1, trainImg2, trainImg3, testImg, nickname
        )
    }
}