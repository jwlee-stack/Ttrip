package org.sfy.ttrip.presentation.init

import android.graphics.Color
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentSignUpInfoBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class SignUpInfoFragment :
    BaseFragment<FragmentSignUpInfoBinding>(R.layout.fragment_sign_up_info) {

    private val viewModel by activityViewModels<UserInfoViewModel>()

    override fun initView() {
        initContent()
    }

    private fun initContent() {
        binding.apply {
            vpContentInfo.adapter = SignUpAdapter(this@SignUpInfoFragment)
            ciBannerInfo.setViewPager(vpContentInfo)

            vpContentInfo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    vpContentInfo.isUserInputEnabled = false

                    when (position) {
                        0 -> {
                            viewModel.isDuplicate.observe(this@SignUpInfoFragment) {
                                it?.let {
                                    if (it) {
                                        checkInfo(false)
                                    } else {
                                        checkInfo(true)
                                    }
                                }
                            }
                        }
                        1 -> {
                            viewModel.userAge.observe(this@SignUpInfoFragment) {
                                if (it == "") checkInfo(false)
                                else checkInfo(true)
                            }
                        }
                        2 -> {
                            //viewModel.nickNameValid.removeObservers(this@SignUpInfoFragment)
                            viewModel.userSex.observe(this@SignUpInfoFragment) {
                                if (it == "") checkInfo(false)
                                else checkInfo(true)
                            }
                        }
                        3 -> {
                            viewModel.profileImgUri.observe(this@SignUpInfoFragment) { uri ->
                                if (uri == null) {
                                    checkInfo(false)
                                } else {
                                    viewModel.userIntro.observe(this@SignUpInfoFragment) {
                                        if (it == "") checkInfo(false)
                                        else checkInfo(true)
                                    }
                                }
                            }
                        }
                        4 -> {

                        }
                    }
                }
            })
        }
    }

    private fun checkInfo(valid: Boolean?) {
        if (valid != null) {
            if (valid) {
                binding.apply {
                    vpContentInfo.isUserInputEnabled = true
                    tvNextInfo.apply {
                        setOnClickListener {
                            vpContentInfo.currentItem = vpContentInfo.currentItem + 1
                        }
                        setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
                        setTextColor(Color.BLACK)
                    }
                }
            } else {
                binding.apply {
                    vpContentInfo.isUserInputEnabled = false
                    tvNextInfo.apply {
                        isEnabled = false
                        setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                        setTextColor(Color.WHITE)
                    }
                }
            }
        } else {
            binding.apply {
                vpContentInfo.isUserInputEnabled = false
                tvNextInfo.apply {
                    isEnabled = false
                    setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                    setTextColor(Color.WHITE)
                }
            }
        }
    }
}