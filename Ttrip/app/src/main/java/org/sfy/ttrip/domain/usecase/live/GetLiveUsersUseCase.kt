package org.sfy.ttrip.domain.usecase.live

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.live.LiveUser
import org.sfy.ttrip.domain.repository.live.LiveRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLiveUsersUseCase @Inject constructor(
    private val liveRepository: LiveRepository
) {
    suspend operator fun invoke(city: String, lng: Double, lat: Double): Resource<List<LiveUser?>> =
        withContext(Dispatchers.IO) {
            liveRepository.getLiveUsers(city, lng, lat)
        }
}