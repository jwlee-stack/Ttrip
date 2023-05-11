package org.sfy.ttrip.data.remote.datasorce.chat

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.chat.CreateChat

data class CreateChatResponse(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("imagePath")
    val imagePath: String?,
    @SerializedName("memberUuid")
    val memberUuid: String,
    @SerializedName("similarity")
    val similarity: Double,
    @SerializedName("lastMessage")
    val lastMessage: String,
    @SerializedName("chatroomId")
    val chatId: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("articleTitle")
    val articleTitle: String,
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("status")
    val status: Char,
    @SerializedName("isMatch")
    val isMatch: Boolean
) : DataToDomainMapper<CreateChat> {
    override fun toDomainModel(): CreateChat =
        CreateChat(
            nickname,
            imagePath,
            memberUuid,
            similarity,
            lastMessage,
            chatId,
            updatedAt,
            articleTitle,
            articleId,
            status,
            isMatch
        )
}