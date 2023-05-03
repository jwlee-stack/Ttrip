package org.sfy.ttrip.domain.entity.board

data class BoardComment(
    val applyId: Int,
    val applicantUuid: String,
    val applicantNickname: String,
    val requestContent: String,
    val imgPath: String,
    val similarity: Float
)
