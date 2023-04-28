package org.sfy.ttrip.presentation.init

import android.graphics.Color
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentSignUpInfoBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class SignUpInfoFragment :
    BaseFragment<FragmentSignUpInfoBinding>(R.layout.fragment_sign_up_info) {

    private val viewModel by viewModels<UserInfoViewModel>()

    override fun initView() {
        initBanner()
        initObserve()
    }

    private fun initBanner() {
        binding.apply {
            vpBannerInfo.adapter = SignUpAdapter(this@SignUpInfoFragment)
            ciBannerInfo.setViewPager(vpBannerInfo)

            vpBannerInfo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    vpBannerInfo.isUserInputEnabled = false
                }
            })

            tvNextInfo.setOnClickListener {
                viewModel.checkNickName()
            }
        }
    }

    private fun initObserve() {
        viewModel.nickNameValid.observe(this) {
            checkInfo(it)
        }

        viewModel.userBirthDay.observe(this) {
            checkInfo(it)
        }

        viewModel.userSex.observe(this) {
            checkInfo(it)
        }

        viewModel.userIntro.observe(this) {
            checkInfo(it)
        }

        viewModel.userBirthDay.observe(this) {
            checkInfo(it)
        }
    }

    private fun checkInfo(valid: Boolean?) {
        if (valid != null) {
            if (valid) {
                binding.apply {
                    vpBannerInfo.isUserInputEnabled = true
                    tvNextInfo.setBackgroundResource(R.drawable.bg_rect_pear_radius10)
                    tvNextInfo.setTextColor(Color.BLACK)
                }
            } else {
                binding.apply {
                    vpBannerInfo.isUserInputEnabled = false
                    tvNextInfo.setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                    tvNextInfo.setTextColor(Color.WHITE)
                }
            }
        } else {
            binding.apply {
                vpBannerInfo.isUserInputEnabled = false
                tvNextInfo.setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                tvNextInfo.setTextColor(Color.WHITE)
            }
        }
    }
}