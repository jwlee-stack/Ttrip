package org.sfy.ttrip.presentation.init

import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentSplashBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    override fun initView() {
        binding.llLogo.setOnClickListener {
            navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingFragment())
        }
    }

    fun goToOnBoarding(){
        navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingFragment())
    }
}