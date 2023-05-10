package org.sfy.ttrip.data.remote.datasorce.chat

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.chat.CreateChat

data class CreateChatResponse(
    @SerializedName("imagePath")
    val imagePath: String?,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("lastMessage")
    val lastMessage: String,
    @SerializedName("chatId")
    val chatId: Int,
    @SerializedName("uuid")
    val uuid: String
) : DataToDomainMapper<CreateChat> {
    override fun toDomainModel(): CreateChat =
        CreateChat(
            imagePath, nickname, updatedAt, lastMessage, chatId, uuid
        )
}