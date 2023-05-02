package org.sfy.ttrip.presentation.mypage

import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentEditProfileBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {

    private val myPageViewModel by activityViewModels<MyPageViewModel>()

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(true)
        initListener()
    }

    private fun initListener() {
        binding.apply {
            tvConfirm.setOnClickListener {
                myPageViewModel.updateUserInfo(
                    etBirthday.text.toString().toInt(),
                    myPageViewModel.genderState,
                    etIntroduction.text.toString(),
                    etNickname.text.toString()
                )
                showToast("수정되었습니다.")
                popBackStack()
            }

            tvGenderFemale.setOnClickListener {
                tvGenderFemale.setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
                tvGenderMale.setBackgroundResource(R.drawable.bg_rect_whisper_radius10)
                myPageViewModel.genderState = "FEMALE"
            }
            tvGenderMale.setOnClickListener {
                tvGenderMale.setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
                tvGenderFemale.setBackgroundResource(R.drawable.bg_rect_whisper_radius10)
                myPageViewModel.genderState = "MALE"
            }
        }
    }
}