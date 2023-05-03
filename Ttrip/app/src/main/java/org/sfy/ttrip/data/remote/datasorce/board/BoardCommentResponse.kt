package org.sfy.ttrip.data.remote.datasorce.board

import com.google.gson.annotations.SerializedName
import org.sfy.ttrip.data.remote.datasorce.base.DataToDomainMapper
import org.sfy.ttrip.domain.entity.board.BoardComment

data class BoardCommentResponse(
    @SerializedName("applyId")
    val applyId: Int,
    @SerializedName("applicantUuid")
    val applicantUuid: String,
    @SerializedName("applicantNickname")
    val applicantNickname: String,
    @SerializedName("requestContent")
    val requestContent: String,
    @SerializedName("imgPath")
    val imgPath: String,
    @SerializedName("similarity")
    val similarity: Float
) : DataToDomainMapper<BoardComment> {
    override fun toDomainModel(): BoardComment =
        BoardComment(
            applyId, applicantUuid, applicantNickname, requestContent, imgPath, similarity
        )
}