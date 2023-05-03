package org.sfy.ttrip.data.remote.datasorce.board

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.board.BoardBrief

data class BoardBriefResponse(
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("authorName")
    val authorName: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("nation")
    val nation: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("dueDay")
    val dueDay: Long,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("status")
    val status: Char
) : DataToDomainMapper<BoardBrief> {
    override fun toDomainModel(): BoardBrief =
        BoardBrief(
            articleId,
            authorName,
            title,
            nation,
            city,
            dueDay,
            startDate,
            endDate,
            createdAt,
            status
        )
}
