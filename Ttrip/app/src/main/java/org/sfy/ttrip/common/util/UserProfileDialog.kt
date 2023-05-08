package org.sfy.ttrip.common.util

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import org.sfy.ttrip.databinding.DialogUserInfoBinding

class UserProfileDialog(
    val activity: Activity,
    private val listener: UserProfileDialogListener,
    val nickname: String,
    val boardId: Int,
    val uuid: String,

) : Dialog(activity) {

    private lateinit var binding: DialogUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(true)
        setCancelable(true)
    }

    private fun init() {
        binding.apply {
            ivPostChat.setOnClickListener {
                listener.postChats(boardId, uuid)
            }
        }
    }
}