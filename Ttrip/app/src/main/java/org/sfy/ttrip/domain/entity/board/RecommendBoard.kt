package org.sfy.ttrip.domain.entity.board

data class RecommendBoard(
    val articleId: Int,
    val authorName: String,
    val imgPath: String,
    val content: String,
    val nation: String,
    val city: String,
    val startDate: String,
    val endDate: String,
    val createdAt: String,
    val dueDay: Long,
    val status: Char,
    val similarity: Float,
)
