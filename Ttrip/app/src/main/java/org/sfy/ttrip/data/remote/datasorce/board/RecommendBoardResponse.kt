package org.sfy.ttrip.data.remote.datasorce.board

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.board.RecommendBoard

data class RecommendBoardResponse(
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("authorName")
    val authorName: String,
    @SerializedName("imgPath")
    val imgPath: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("nation")
    val nation: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("dueDay")
    val dueDay: Long,
    @SerializedName("status")
    val status: Char,
    @SerializedName("similarity")
    val similarity: Float,
) : DataToDomainMapper<RecommendBoard> {
    override fun toDomainModel(): RecommendBoard =
        RecommendBoard(
            articleId,
            authorName,
            imgPath,
            title,
            content,
            nation,
            city,
            startDate,
            endDate,
            createdAt,
            dueDay,
            status,
            similarity
        )
}

