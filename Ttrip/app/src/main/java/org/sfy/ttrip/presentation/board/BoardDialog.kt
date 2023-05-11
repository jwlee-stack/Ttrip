package org.sfy.ttrip.presentation.board

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import org.sfy.ttrip.databinding.DialogDeleteBoardBinding

class BoardDialog(
    val activity: Activity,
    private val listener: BoardDialogListener,
    private val boardId: Int,
    private val boolean: Boolean
) : Dialog(activity) {

    private lateinit var binding: DialogDeleteBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogDeleteBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(true)
        setCancelable(true)

        binding.apply {
            if (boolean) {
                tvBoardDetailDialog.text = "게시글을 삭제하시겠습니까?"
                tvCancel.setOnClickListener {
                    dismiss()
                }

                tvConfirm.setOnClickListener {
                    listener.deleteBoard(boardId)
                    dismiss()
                }
            } else {
                tvBoardDetailDialog.text = "게시글 모집을 종료하시겠습니까?"
                tvCancel.setOnClickListener {
                    dismiss()
                }

                tvConfirm.setOnClickListener {
                    listener.finishBoard(boardId)
                    dismiss()
                }
            }
        }
    }
}