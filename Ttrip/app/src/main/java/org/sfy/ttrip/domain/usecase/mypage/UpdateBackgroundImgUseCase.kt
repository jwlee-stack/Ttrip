package org.sfy.ttrip.domain.usecase.mypage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.mypage.BackgroundImg
import org.sfy.ttrip.domain.repository.mypage.MyPageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateBackgroundImgUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        backgroundImg: MultipartBody.Part?
    ): Resource<BackgroundImg> = withContext(Dispatchers.IO) {
        myPageRepository.updateBackgroundImg(backgroundImg)
    }
}