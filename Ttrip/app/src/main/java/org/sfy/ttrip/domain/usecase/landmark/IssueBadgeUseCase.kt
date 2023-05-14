package org.sfy.ttrip.domain.usecase.landmark

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.domain.repository.landmark.LandmarkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IssueBadgeUseCase @Inject constructor(
    private val landmarkRepository: LandmarkRepository
) {
    suspend operator fun invoke(landmarkId: Int): Int =
        withContext(Dispatchers.IO) {
            landmarkRepository.issueBadges(landmarkId)
        }
}