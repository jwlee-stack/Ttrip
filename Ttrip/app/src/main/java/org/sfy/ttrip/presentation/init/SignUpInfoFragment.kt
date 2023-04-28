package org.sfy.ttrip.presentation.init

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
            when (it) {
                true -> {
                    binding.apply {
                        vpBannerInfo.isUserInputEnabled = true
                        //tvNextInfo.setBackgroundColor(pear)
                    }
                }
                false -> {
                    binding.vpBannerInfo.isUserInputEnabled = false
                }
                else -> {}
            }
        }
    }
}