package org.sfy.ttrip.data.remote.datasorce.landmark

import com.google.gson.annotations.SerializedName

data class BadgeRequest(
    @SerializedName("landmarkId")
    val landmarkId: Int
)
