package org.sfy.ttrip.data.remote.datasorce.board

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class PostBoardRequest(
    @SerializedName("content")
    val content: String,
    @SerializedName("nation")
    val nation: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("startDate")
    val startDate: LocalDateTime,
    @SerializedName("endDate")
    val endDate: LocalDateTime,
)