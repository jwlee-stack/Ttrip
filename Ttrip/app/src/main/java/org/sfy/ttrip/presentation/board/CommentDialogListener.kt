package org.sfy.ttrip.presentation.board

interface CommentDialogListener {
    fun addComment(boardId: Int, content: String?)
}