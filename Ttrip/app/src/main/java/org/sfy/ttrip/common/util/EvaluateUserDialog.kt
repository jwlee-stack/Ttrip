package org.sfy.ttrip.common.util

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import org.sfy.ttrip.databinding.DialogEvaluateBinding

class EvaluateUserDialog(
    val activity: Activity,
    private val listener: EvaluateUserDialogListener,
    private val userNickname: String,
    private val matchHistoryId: String
) : Dialog(activity) {

    private lateinit var binding: DialogEvaluateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogEvaluateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(true)
        setCancelable(true)

        initListener()
        initView()
    }

    private fun initListener() {
        binding.apply {
            tvCancel.setOnClickListener {
                dismiss()
            }

            tvConfirm.setOnClickListener {
                if (binding.rbUserEvaluateRate.rating.toInt() == 0) {
                    activity.showToastMessage("평점을 입력해주세요!")
                } else {
                    activity.showToastMessage("리뷰가 완료되었습니다")
                    listener.evaluate(matchHistoryId, binding.rbUserEvaluateRate.rating.toInt())
                    dismiss()
                }
            }

            clDeclaration.setOnClickListener {
                dismiss()
                listener.openDeclaration(userNickname)
            }
        }
    }

    private fun initView() {
        binding.tvNickname.text = this.userNickname
    }
}