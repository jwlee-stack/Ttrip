package org.sfy.ttrip.data.remote.datasorce.chat

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.chat.ChatRoom

data class ChatRoomResponse(
    @SerializedName("imagePath")
    val imagePath: String?,
    @SerializedName("nickname")
    val otherNickname: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("lastMessage")
    val lastMessage: String,
    @SerializedName("chatroomId")
    val chatId: Int,
    @SerializedName("memberUuid")
    val otherUuid: String,
    @SerializedName("articleTitle")
    val articleTitle: String,
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("isMatch")
    val isMatch: Boolean
) : DataToDomainMapper<ChatRoom> {
    override fun toDomainModel(): ChatRoom =
        ChatRoom(
            imagePath,
            otherNickname,
            updatedAt,
            lastMessage,
            chatId,
            otherUuid,
            articleTitle,
            articleId,
            status,
            isMatch
        )
}