package org.sfy.ttrip.domain.entity.board

data class PostBoard(
    val articleId: Int,
    val city: String,
    val content: String?,
    val authorId: Int,
)
