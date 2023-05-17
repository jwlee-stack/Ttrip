package org.sfy.ttrip.common.util

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import org.sfy.ttrip.databinding.DialogDeclarationBinding

class DeclarationDialog(
    val activity: Activity,
    private val listener: DeclarationDialogListener,
    private val reportedNickname: String,
) : Dialog(activity) {
    private lateinit var binding: DialogDeclarationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogDeclarationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(true)
        setCancelable(true)

        initListener()
    }

    private fun initListener() {
        binding.apply {
            tvCancelDeclaration.setOnClickListener {
                listener.cancelDeclaration()
                dismiss()
            }

            tvConfirmDeclaration.setOnClickListener {
                if (etDeclarationContent.text.toString() == "") {
                    activity.showToastMessage("신고 내용을 꼭 입력해주세요!")
                } else {
                    listener.postDeclaration(etDeclarationContent.text.toString(), reportedNickname)
                    dismiss()
                }
            }
        }
    }
}