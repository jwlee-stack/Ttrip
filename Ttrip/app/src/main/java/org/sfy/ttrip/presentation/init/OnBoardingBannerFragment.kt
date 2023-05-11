package org.sfy.ttrip.presentation.init

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentOnboardingBannerBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class OnBoardingBannerFragment :
    BaseFragment<FragmentOnboardingBannerBinding>(R.layout.fragment_onboarding_banner) {

    private var bannerPosition = -1

    override fun initView() {

        val bannerData = listOf(
            R.drawable.ic_onboarding_puzzles,
            R.drawable.ic_onboarding_task,
            R.drawable.ic_onboarding_travel
        )

        bannerPosition = arguments?.getInt("banner_position")!!
        with(bannerData[bannerPosition]) {
            binding.ivOnboardingBanner.setImageResource(bannerData[bannerPosition])
            when (bannerPosition) {
                0 -> {
                    binding.apply {
                        tvOnboardingPuzzles1.visibility = View.VISIBLE
                        llOnboardingPuzzles2.visibility = View.VISIBLE

                        llOnboardingTask1.visibility = View.GONE
                        llOnboardingTask2.visibility = View.GONE

                        llOnboardingTravel1.visibility = View.GONE
                        llOnboardingTravel2.visibility = View.GONE
                    }
                }
                1 -> {
                    binding.apply {
                        tvOnboardingPuzzles1.visibility = View.GONE
                        llOnboardingPuzzles2.visibility = View.GONE

                        llOnboardingTask1.visibility = View.VISIBLE
                        llOnboardingTask2.visibility = View.VISIBLE

                        llOnboardingTravel1.visibility = View.GONE
                        llOnboardingTravel2.visibility = View.GONE
                    }
                }
                2 -> {
                    binding.apply {
                        tvOnboardingPuzzles1.visibility = View.GONE
                        llOnboardingPuzzles2.visibility = View.GONE

                        llOnboardingTask1.visibility = View.GONE
                        llOnboardingTask2.visibility = View.GONE

                        llOnboardingTravel1.visibility = View.VISIBLE
                        llOnboardingTravel2.visibility = View.VISIBLE

                        (parentFragment as OnBoardingFragment).eraseIndicator()

                        llStartLogin.visibility = View.VISIBLE
                        tvStartLogin.visibility = View.VISIBLE

                        tvStartLogin.setOnClickListener {
                            navigate(OnBoardingFragmentDirections.actionOnboardingFragmentToLoginFragment())
                        }

                        tvGoToSignUp.setOnClickListener {
                            navigate(OnBoardingFragmentDirections.actionOnboardingFragmentToSignUpFragment())
                        }
                    }
                }
                else -> {}
            }
        }
    }
}