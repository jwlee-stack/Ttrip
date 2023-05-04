package org.sfy.ttrip.data.remote.datasorce.board

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("requestContent")
    val requestContent: String
)
