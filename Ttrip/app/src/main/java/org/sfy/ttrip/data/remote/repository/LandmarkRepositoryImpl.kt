package org.sfy.ttrip.data.remote.repository

import okhttp3.MultipartBody
import org.sfy.ttrip.common.util.wrapToResource
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.landmark.BadgeRequest
import org.sfy.ttrip.data.remote.datasorce.landmark.LandmarkRemoteDataSource
import org.sfy.ttrip.domain.entity.landmark.DoodleItem
import org.sfy.ttrip.domain.entity.landmark.BadgeItem
import org.sfy.ttrip.domain.entity.landmark.LandmarkItem
import org.sfy.ttrip.domain.repository.landmark.LandmarkRepository
import javax.inject.Inject

class LandmarkRepositoryImpl @Inject constructor(
    private val landmarkRemoteDataSource: LandmarkRemoteDataSource
) : LandmarkRepository {

    override suspend fun getLandmarks(): Resource<List<LandmarkItem>> = wrapToResource {
        landmarkRemoteDataSource.getLandmarks().map { it.toDomainModel() }
    }

    override suspend fun createDoodle(
        positionX: Double,
        positionY: Double,
        positionZ: Double,
        doodleImgPath: MultipartBody.Part?,
        landmarkId: Int,
        latitude: Double,
        longitude: Double
    ) {
        landmarkRemoteDataSource.createDoodle(
            positionX, positionY, positionZ, doodleImgPath, landmarkId, latitude, longitude
        )
    }

    override suspend fun getDoodles(landmarkId: Int): Resource<List<DoodleItem>> = wrapToResource {
        landmarkRemoteDataSource.getDoodles(landmarkId).map { it.toDomainModel() }
    }

    override suspend fun getBadges(): Resource<List<BadgeItem>> = wrapToResource {
        landmarkRemoteDataSource.getBadges().map { it.toDomainModel() }
    }

    override suspend fun issueBadges(landmarkId: Int): Int =
        landmarkRemoteDataSource.issueBadge(BadgeRequest(landmarkId))
}