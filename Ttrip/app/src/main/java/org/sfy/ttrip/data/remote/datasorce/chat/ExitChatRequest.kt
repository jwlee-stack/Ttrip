package org.sfy.ttrip.data.remote.datasorce.chat

import com.google.gson.annotations.SerializedName

data class ExitChatRequest(
    @SerializedName("chatroomId")
    val chatRoomId: Int
)
