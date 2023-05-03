package org.sfy.ttrip.domain.repository.live

import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.live.CallToken
import org.sfy.ttrip.domain.entity.live.LiveUser
import org.sfy.ttrip.domain.entity.live.SessionItem

interface LiveRepository {

    suspend fun getLiveUsers(
        city: String,
        lng: Double,
        lat: Double
    ): Resource<List<LiveUser?>>

    suspend fun createSession(): Resource<SessionItem>

    suspend fun getCallToken(sessionId: String, memberUuid: String): Resource<CallToken>
}