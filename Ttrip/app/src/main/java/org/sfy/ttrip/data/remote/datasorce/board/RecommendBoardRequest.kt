package org.sfy.ttrip.data.remote.datasorce.board

import com.google.gson.annotations.SerializedName

data class RecommendBoardRequest(
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("authorId")
    val authorId: Int,
    @SerializedName("city")
    val city: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("numOfArticles")
    val numOfArticles: Int,
)
