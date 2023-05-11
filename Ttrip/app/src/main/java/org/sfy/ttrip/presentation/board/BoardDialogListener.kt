package org.sfy.ttrip.presentation.board

interface BoardDialogListener {
    fun deleteBoard(boardId: Int)
    fun finishBoard(boardId: Int)
}