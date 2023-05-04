package org.sfy.ttrip.data.remote.datasorce.chat

import com.google.gson.annotations.SerializedName

data class ChatMatchRequest(
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("opponentUserUuid")
    val opponentUserUuid: String
)
