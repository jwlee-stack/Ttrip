package org.sfy.ttrip.presentation.mypage

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
        setTextWatcher()
        validCheck()
    }

    private fun initListener() {
        binding.apply {
            tvConfirm.setOnClickListener {
                myPageViewModel.updateUserInfo(
                    etBirthday.text.toString().toInt(),
                    myPageViewModel.gender.value.toString(),
                    etIntroduction.text.toString(),
                    etNickname.text.toString()
                )
                showToast("수정되었습니다.")
                popBackStack()
            }

            tvCheckDuplication.setOnClickListener {
                if (myPageViewModel.nickname.value == null) {
                    showToast("닉네임을 입력해주세요.")
                } else {
                    lifecycleScope.launch {
                        val async = myPageViewModel.checkDuplication()
                        showDuplicateInfo(async)
                    }
                }
            }

            tvGenderFemale.setOnClickListener {
                selectFemale()
            }
            tvGenderMale.setOnClickListener {
                selectMale()
            }
            ivBack.setOnClickListener { popBackStack() }
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

    private fun setTextWatcher() {
        binding.apply {
            etNickname.addTextChangedListener {
                if (binding.etNickname.text.toString().length > 6) {
                    showToast("닉네임은 6글자 이내로 가능합니다.")
                } else {
                    myPageViewModel.postNickname(binding.etNickname.text.toString())
                    myPageViewModel.returnDuplicationTrue()
                }
            }

            etBirthday.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    myPageViewModel.postAge(binding.etBirthday.text.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                    myPageViewModel.postAge(binding.etBirthday.text.toString())
                }
            })

            etIntroduction.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    myPageViewModel.postIntro(binding.etIntroduction.text.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                    myPageViewModel.postIntro(binding.etIntroduction.text.toString())
                }
            })
        }
    }

    private fun showDuplicateInfo(async: Int) {
        if (myPageViewModel.isDuplicate.value == true) {
            binding.tvNicknameDescription.visibility = View.GONE
        } else {
            binding.tvNicknameDescription.visibility = View.VISIBLE
        }
    }

    private fun validCheck() {
        myPageViewModel.isDuplicate.observe(viewLifecycleOwner) { isDuplicate ->
            myPageViewModel.age.observe(viewLifecycleOwner) { age ->
                if (isDuplicate!!.not() && age.toString() != "") {
                    binding.tvConfirm.apply {
                        setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
                        isEnabled = true
                    }
                } else {
                    binding.tvConfirm.apply {
                        setBackgroundResource(R.drawable.bg_rect_whisper_radius10)
                        isEnabled = false
                    }
                }
            }
        }
    }

    private fun selectFemale() {
        binding.apply {
            tvGenderFemale.setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
            tvGenderMale.setBackgroundResource(R.drawable.bg_rect_whisper_radius10)
        }
        myPageViewModel.postGender("FEMALE")
    }

    private fun selectMale() {
        binding.apply {
            tvGenderMale.setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
            tvGenderFemale.setBackgroundResource(R.drawable.bg_rect_whisper_radius10)
        }
        myPageViewModel.postGender("MALE")
    }
}