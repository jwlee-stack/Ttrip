package org.sfy.ttrip.domain.entity.board

data class BoardBrief(
    val articleId: Int,
    val authorName: String,
    val title: String,
    val nation: String,
    val city: String,
    val dueDay: Long,
    val startDate: String,
    val endDate: String,
    val createdAt: String,
    val status: Char
)