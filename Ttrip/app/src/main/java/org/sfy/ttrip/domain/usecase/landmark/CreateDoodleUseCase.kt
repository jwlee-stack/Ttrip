package org.sfy.ttrip.domain.usecase.landmark

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.sfy.ttrip.domain.repository.landmark.LandmarkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateDoodleUseCase @Inject constructor(
    private val landmarkRepository: LandmarkRepository
) {
    suspend operator fun invoke(
        positionX: Double,
        positionY: Double,
        positionZ: Double,
        doodleImgPath: MultipartBody.Part?,
        landmarkId: Int,
        latitude: Double,
        longitude: Double
    ) = withContext(Dispatchers.IO) {
        landmarkRepository.createDoodle(
            positionX, positionY, positionZ, doodleImgPath, landmarkId, latitude, longitude
        )
    }
}