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
        initBanner()
    }

    private fun initBanner() {
        binding.apply {
            vpBannerInfo.adapter = SignUpAdapter(this@SignUpInfoFragment)
            ciBannerInfo.setViewPager(vpBannerInfo)

            vpBannerInfo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    vpBannerInfo.isUserInputEnabled = false

                    when (position) {
                        0 -> {
                            viewModel.nickNameValid.observe(this@SignUpInfoFragment) {
                                checkInfo(it)
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
                                when (uri) {
                                    null -> {
                                        checkInfo(false)
                                    }
                                    else -> {
                                        viewModel.userIntro.observe(this@SignUpInfoFragment) {
                                            if (it == "") checkInfo(false)
                                            else checkInfo(true)
                                        }
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
                    vpBannerInfo.isUserInputEnabled = true
                    tvNextInfo.apply {
                        setOnClickListener {
                            vpBannerInfo.currentItem = vpBannerInfo.currentItem + 1
                        }
                        setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
                        setTextColor(Color.BLACK)
                    }
                }
            } else {
                binding.apply {
                    vpBannerInfo.isUserInputEnabled = false
                    tvNextInfo.apply {
                        setOnClickListener {}
                        setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                        setTextColor(Color.WHITE)
                    }
                }
            }
        } else {
            binding.apply {
                vpBannerInfo.isUserInputEnabled = false
                tvNextInfo.apply {
                    setOnClickListener {}
                    setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                    setTextColor(Color.WHITE)
                }
            }
        }
    }
}