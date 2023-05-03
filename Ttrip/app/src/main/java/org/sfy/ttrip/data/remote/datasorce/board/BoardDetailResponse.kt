package org.sfy.ttrip.data.remote.datasorce.board

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.board.BoardDetail

data class BoardDetailResponse(
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("authorName")
    val authorName: String,
    @SerializedName("imgPath")
    val imgPath: String?,
    @SerializedName("createdAt")
    val createdAt: String,
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
    @SerializedName("status")
    val status: Char,
    @SerializedName("isMine")
    val isMine: Boolean,
    @SerializedName("isApplied")
    val isApplied: Boolean,
    @SerializedName("similarity")
    val similarity: Int
) : DataToDomainMapper<BoardDetail> {
    override fun toDomainModel(): BoardDetail =
        BoardDetail(
            articleId,
            authorName,
            imgPath,
            createdAt,
            title,
            content,
            nation,
            city,
            startDate,
            endDate,
            status,
            isMine,
            isApplied,
            similarity
        )
}
