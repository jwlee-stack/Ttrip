package org.sfy.ttrip.presentation.board

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import org.sfy.ttrip.databinding.DialogPostBoardCommentBinding

class CommentDialog(
    val activity: Activity,
    private val listener: CommentDialogListener,
    private val boardId: Int,
) : Dialog(activity) {

    private lateinit var binding: DialogPostBoardCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPostBoardCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(true)
        setCancelable(true)

        binding.apply {
            tvCancelComment.setOnClickListener {
                dismiss()
            }

            tvConfirmComment.setOnClickListener {
                listener.addComment(boardId, etBoardCommentContent.text.toString())
                dismiss()
            }
        }
    }
}