package org.sfy.ttrip.data.remote.datasorce.board

import com.google.gson.annotations.SerializedName

data class SearchBoardRequest(
    @SerializedName("city")
    val city: String,
    @SerializedName("condition")
    val condition: Int,
    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("nation")
    val nation: String
)
