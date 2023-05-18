package org.sfy.ttrip.domain.repository.landmark

import okhttp3.MultipartBody
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.landmark.DoodleItem
import org.sfy.ttrip.domain.entity.landmark.BadgeItem
import org.sfy.ttrip.domain.entity.landmark.LandmarkItem

interface LandmarkRepository {

    suspend fun getLandmarks(): Resource<List<LandmarkItem>>

    suspend fun createDoodle(
        positionX: Double,
        positionY: Double,
        positionZ: Double,
        doodleImgPath: MultipartBody.Part?,
        landmarkId: Int,
        latitude: Double,
        longitude: Double
    )

    suspend fun getDoodles(landmarkId: Int): Resource<List<DoodleItem>>

    suspend fun getBadges(): Resource<List<BadgeItem>>

    suspend fun issueBadges(landmarkId: Int): Int
}