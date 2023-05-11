package org.sfy.ttrip.data.remote.datasorce.chat

import com.google.gson.annotations.SerializedName

data class ChatMatchRequest(
    @SerializedName("chatroomId")
    val articleId: Int,
    @SerializedName("opponentUuid")
    val opponentUserUuid: String
)