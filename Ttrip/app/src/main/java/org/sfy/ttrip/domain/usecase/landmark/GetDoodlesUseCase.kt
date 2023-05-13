package org.sfy.ttrip.domain.usecase.landmark

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.landmark.DoodleItem
import org.sfy.ttrip.domain.repository.landmark.LandmarkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDoodlesUseCase @Inject constructor(
    private val landmarkRepository: LandmarkRepository
) {
    suspend operator fun invoke(landmarkId: Int): Resource<List<DoodleItem>> =
        withContext(Dispatchers.IO) {
            landmarkRepository.getDoodles(landmarkId)
        }
}