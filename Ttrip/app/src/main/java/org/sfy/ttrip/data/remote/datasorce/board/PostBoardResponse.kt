package org.sfy.ttrip.data.remote.datasorce.board

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.board.PostBoard

data class PostBoardResponse(
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("city")
    val city: String,
    @SerializedName("content")
    val content: String?,
    @SerializedName("authorId")
    val authorId: Int,
) : DataToDomainMapper<PostBoard> {
    override fun toDomainModel(): PostBoard =
        PostBoard(articleId, city, content, authorId)
}