package org.sfy.ttrip.data.remote.datasorce.chat

import com.google.gson.annotations.SerializedName

data class ChatMatchRequest(
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("opponentUuid")
    val opponentUserUuid: String
)