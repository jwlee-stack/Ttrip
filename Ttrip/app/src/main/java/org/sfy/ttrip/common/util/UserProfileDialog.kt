package org.sfy.ttrip.common.util

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import org.sfy.ttrip.common.util.BindingAdapters.setProfileImg
import org.sfy.ttrip.databinding.DialogUserInfoBinding

class UserProfileDialog(
    val activity: Activity,
    private val listener: UserProfileDialogListener,
    private val nickname: String,
    private val boardId: Int,
    private val uuid: String,
    private val backgroundImgPath: String,
    private val profileImgPath: String,
    private val similarity: Float,
    private val age: Int,
    private val gender: String,
    private val intro: String,
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

        initView()
        initListener()
    }

    private fun initListener() {
        binding.apply {
            ivPostChat.setOnClickListener {
                listener.postChats(boardId, uuid)
            }
        }
    }

    private fun initView() {
        binding.apply {
            tvUserNickName.text = nickname
            ivUserProfile.setProfileImg(profileImgPath)
            ivUserProfileBackground.setProfileImg(backgroundImgPath)
            tvUserInfoContent.text = intro
            tvProfileInfoAge.text = age.toString()

            if (gender == "FEMALE") {
                tvProfileInfoGender.text = "여성"
            } else {
                tvProfileInfoGender.text = "남성"
            }

            if (similarity)
        }
    }
}