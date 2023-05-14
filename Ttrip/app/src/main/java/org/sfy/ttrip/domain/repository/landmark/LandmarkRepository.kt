package org.sfy.ttrip.domain.repository.landmark

import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.landmark.BadgeItem
import org.sfy.ttrip.domain.entity.landmark.LandmarkItem

interface LandmarkRepository {

    suspend fun getLandmarks(): Resource<List<LandmarkItem>>

    suspend fun getBadges(): Resource<List<BadgeItem>>

    suspend fun issueBadges(landmarkId: Int): Int
}