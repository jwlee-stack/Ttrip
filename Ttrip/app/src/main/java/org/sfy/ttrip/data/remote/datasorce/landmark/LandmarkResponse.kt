package org.sfy.ttrip.data.remote.datasorce.landmark

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.landmark.LandmarkItem

data class LandmarkResponse(
    @SerializedName("landmarkId")
    val landmarkId: Int,
    @SerializedName("landmarkName")
    val landmarkName: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
) : DataToDomainMapper<LandmarkItem> {
    override fun toDomainModel(): LandmarkItem =
        LandmarkItem(
            landmarkId, landmarkName, latitude, longitude
        )
}