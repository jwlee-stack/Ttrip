package org.sfy.ttrip.domain.repository.live

import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.live.LiveUser
import org.sfy.ttrip.domain.entity.live.SessionItem

interface LiveRepository {

    suspend fun getLiveUsers(
        city: String,
        lng: Double,
        lat: Double
    ): Resource<List<LiveUser?>>

    suspend fun createSession(): Resource<SessionItem>
}