package org.sfy.ttrip.data.remote.datasorce.chat

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.chat.ChatRoom
import java.time.LocalDateTime

data class ChatRoomResponse(
    @SerializedName("imagePath")
    val imagePath: String?,
    @SerializedName("nickname")
    val otherNickname: String,
    @SerializedName("updatedAt")
    val updatedAt: LocalDateTime,
    @SerializedName("lastMessage")
    val lastMessage: String,
    @SerializedName("chatId")
    val chatId: Int,
    @SerializedName("uuid")
    val otherUuid: String
) : DataToDomainMapper<ChatRoom> {
    override fun toDomainModel(): ChatRoom =
        ChatRoom(imagePath, otherNickname, updatedAt, lastMessage, chatId, otherUuid)
}