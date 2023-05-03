package org.sfy.ttrip.domain.entity.board


data class BoardDetail(
    val articleId: Int,
    val authorName: String,
    val imgPath: String,
    val createdDate: String,
    val title: String,
    val content: String,
    val nation: String,
    val city: String,
    val startDate: String,
    val endDate: String,
    val status: Char,
    val isMine: Boolean,
    val isApplied: Boolean,
    val similarity: Int
)
