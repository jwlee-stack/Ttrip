package org.sfy.ttrip.data.remote.datasorce.chat

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.chat.ChatDetail

data class ChatDetailResponse(
    @SerializedName("isMine")
    val isMine: Boolean,
    @SerializedName("content")
    val content: String,
    @SerializedName("createdAt")
    val createdDate: String
) : DataToDomainMapper<ChatDetail> {
    override fun toDomainModel(): ChatDetail =
        ChatDetail(isMine, content, createdDate)
}