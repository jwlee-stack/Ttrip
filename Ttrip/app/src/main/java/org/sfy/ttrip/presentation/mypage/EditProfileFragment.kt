package org.sfy.ttrip.presentation.mypage

import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentEditProfileBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {

    private val myPageViewModel by activityViewModels<MyPageViewModel>()

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(true)
        initProfile()
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
                selectFemale()
            }
            tvGenderMale.setOnClickListener {
                selectMale()
            }
        }
    }

    private fun initProfile() {
        binding.apply {
            etNickname.setText(myPageViewModel.userProfile.value!!.nickname.toString())
            etBirthday.setText(myPageViewModel.userProfile.value!!.age.toString())
            etIntroduction.setText(myPageViewModel.userProfile.value!!.intro.toString())
            if (myPageViewModel.userProfile.value!!.gender == "FEMALE") {
                selectFemale()
            } else {
                selectMale()
            }
        }
    }

    private fun selectFemale() {
        binding.apply {
            tvGenderFemale.setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
            tvGenderMale.setBackgroundResource(R.drawable.bg_rect_whisper_radius10)
        }
        myPageViewModel.genderState = "FEMALE"
    }

    private fun selectMale() {
        binding.apply {
            tvGenderMale.setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
            tvGenderFemale.setBackgroundResource(R.drawable.bg_rect_whisper_radius10)
        }
        myPageViewModel.genderState = "MALE"
    }
}