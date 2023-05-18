package org.sfy.ttrip.presentation.mypage

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import org.sfy.ttrip.ApplicationClass
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.DialogLogoutBinding

class LogoutDialog(
    context: Context,
    private val listener: LogoutDialogListener
) : Dialog(context) {

    private lateinit var binding:  DialogLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_logout,
            null, false
        )

        setContentView(binding.root)

        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(true)
        setCancelable(true)

        binding.apply {
            tvConfirm.setOnClickListener {
                listener.onConfirmButtonClicked()
                ApplicationClass.preferences.clearPreferences()
                dismiss()
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
    }
}
