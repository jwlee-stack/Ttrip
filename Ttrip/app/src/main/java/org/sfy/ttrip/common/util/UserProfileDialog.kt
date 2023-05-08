package org.sfy.ttrip.common.util

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import org.sfy.ttrip.R
import org.sfy.ttrip.common.util.BindingAdapters.setNormalImg
import org.sfy.ttrip.common.util.BindingAdapters.setProfileImg
import org.sfy.ttrip.common.util.BindingAdapters.setProfileImg
import org.sfy.ttrip.databinding.DialogUserInfoBinding

class UserProfileDialog(
    val activity: Activity,
    private val listener: UserProfileDialogListener,
    private val nickname: String,
    private val boardId: Int,
    private val uuid: String,
    private val backgroundImgPath: String?,
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
            ivCloseDialog.setOnClickListener {
                listener.clear()
                dismiss()
            }
        }
    }

    private fun initView() {
        binding.apply {
            tvUserNickName.text = nickname
            ivUserProfile.setProfileImg(profileImgPath)
            ivUserProfileBackground.setNormalImg(backgroundImgPath)
            tvUserInfoContent.text = intro
            tvProfileInfoAge.text = age.toString()
            tvUserProfileSimilarity.text = "${similarity.toInt()}%"

            if (gender == "FEMALE") {
                tvProfileInfoGender.text = "여성"
            } else {
                tvProfileInfoGender.text = "남성"
            }

            if (similarity <= 50) {
                tvUserProfileSimilarity.setBackgroundResource(R.drawable.bg_rect_lochmara2_alice_blue2_radius10_stroke1)
                tvUserProfileSimilarity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.lochmara
                    )
                )
            } else if (similarity <= 80) {
                tvUserProfileSimilarity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.limerick
                    )
                )
                tvUserProfileSimilarity.setBackgroundResource(R.drawable.bg_rect_limerick_twilight_blue_radius10_stroke1)
            } else {
                tvUserProfileSimilarity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.medium_orchid
                    )
                )
                tvUserProfileSimilarity.setBackgroundResource(R.drawable.bg_rect_medium_orchid_white_lilac_radius10_stroke1)
            }
        }
    }
}