package org.sfy.ttrip.data.remote.datasorce.board

import com.google.gson.annotations.SerializedName

data class BoardDetailResponse(
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("authorName")
    val authorName: String,
    @SerializedName("createdDate")
    val createdDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("nation")
    val nation: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("status")
    val status: Char,
    @SerializedName("isMine")
    val isMine: Boolean,
)
