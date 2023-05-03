package org.sfy.ttrip.data.remote.repository

import org.sfy.ttrip.common.util.wrapToResource
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.live.LiveRemoteDataSource
import org.sfy.ttrip.domain.entity.live.LiveUser
import org.sfy.ttrip.domain.entity.live.SessionItem
import org.sfy.ttrip.domain.repository.live.LiveRepository
import javax.inject.Inject

class LiveRepositoryImpl @Inject constructor(
    private val liveRemoteDataSource: LiveRemoteDataSource
) : LiveRepository {

    override suspend fun getLiveUsers(
        city: String,
        lng: Double,
        lat: Double
    ): Resource<List<LiveUser?>> =
        wrapToResource {
            liveRemoteDataSource.getLiveUsers(city, lng, lat).map { it?.toDomainModel() }
        }

    override suspend fun createSession(): Resource<SessionItem> =
        wrapToResource {
            liveRemoteDataSource.createSession().toDomainModel()
        }
}