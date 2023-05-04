package org.sfy.ttrip.data.remote.datasorce.chat

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.chat.ChatDetail
import java.time.LocalDateTime

data class ChatDetailResponse(
    @SerializedName("isMine")
    val isMine: Boolean,
    @SerializedName("content")
    val content: String,
    @SerializedName("createdDate")
    val createdDate: LocalDateTime
) : DataToDomainMapper<ChatDetail> {
    override fun toDomainModel(): ChatDetail =
        ChatDetail(isMine, content, createdDate)
}