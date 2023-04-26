package org.sfy.ttrip.presentation.init

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentOnboardingBinding
import org.sfy.ttrip.presentation.base.BaseFragment

class OnBoardingFragment : BaseFragment<FragmentOnboardingBinding>(R.layout.fragment_onboarding) {

    override fun initView() {
        initBanner()
    }

    private fun initBanner() {
        binding.apply {
            vpBanner.adapter = OnBoardingAdapter(this@OnBoardingFragment)
            ciBanner.setViewPager(binding.vpBanner)

            vpBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    vpBanner.isUserInputEnabled = position != 2
                }
            })

            tvSkipOnboarding.setOnClickListener {
                vpBanner.currentItem = 2
            }

            ivOnboardingLoader.setOnClickListener {
                vpBanner.currentItem = 2
            }
        }
    }

    fun eraseIndicator() {
        binding.apply {
            ciBanner.visibility = View.GONE
            tvSkipOnboarding.visibility = View.GONE
            ivOnboardingLoader.visibility = View.GONE
        }
    }
}