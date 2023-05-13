package org.sfy.ttrip.data.remote.datasorce.landmark

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.landmark.DoodleItem

data class DoodleResponse(
    @SerializedName("doodleId")
    val doodleId: Int,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("positionX")
    val positionX: Double,
    @SerializedName("positionY")
    val positionY: Double,
    @SerializedName("positionZ")
    val positionZ: Double,
    @SerializedName("doodleImgPath")
    val doodleImgPath: String,
    @SerializedName("landmarkId")
    val landmarkId: Int
) : DataToDomainMapper<DoodleItem> {
    override fun toDomainModel(): DoodleItem =
        DoodleItem(doodleId, latitude, positionX, positionY, positionZ, doodleImgPath, landmarkId)
}