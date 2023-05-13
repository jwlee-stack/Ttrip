package org.sfy.ttrip.presentation.landmark

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.DialogDrawDoodleBinding

class DrawDoodleDialog(
    context: Context,
    private val listener: DrawDoodleDialogListener
) : Dialog(context) {

    private lateinit var binding:  DialogDrawDoodleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_draw_doodle,
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
                val bitmap = drawingView.getBitmap()
                if (bitmap != null) {
                    listener.onConfirmButtonClicked(bitmap)
                }
                dismiss()
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
            ivDoodleClear.setOnClickListener {
                drawingView.reset()
            }
            ivDoodleColorBlack.setOnClickListener {
                drawingView.setColorBlack()
            }
            ivDoodleColorNeonBlue.setOnClickListener {
                drawingView.setColorNeonBlue()
            }
            ivDoodleColorPear.setOnClickListener {
                drawingView.setColorPear()
            }
        }
    }
}
