package org.sfy.ttrip.data.remote.datasorce.landmark

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.landmark.BadgeItem

data class BadgeResponse(
    @SerializedName("badgeId")
    val badgeId: Int,
    @SerializedName("badgeName")
    val badgeName: String,
    @SerializedName("badgeImgPath")
    val badgeImgPath: String
): DataToDomainMapper<BadgeItem> {
    override fun toDomainModel(): BadgeItem =
        BadgeItem(badgeId, badgeName, badgeImgPath)
}