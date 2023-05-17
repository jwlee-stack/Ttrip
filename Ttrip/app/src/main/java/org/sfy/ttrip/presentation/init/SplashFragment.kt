package org.sfy.ttrip.presentation.init

import android.animation.ObjectAnimator
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    override fun onResume() {
        super.onResume()
        splashAnimation()
        initObserver()
    }

    private fun splashAnimation() {
        CoroutineScope(Dispatchers.Main).launch {
            val animatorBack =
                ObjectAnimator.ofFloat(binding.ivTicketBack, "translationX", -1000f, 0f)
            animatorBack.duration = 1000
            animatorBack.start()

            val animatorFront =
                ObjectAnimator.ofFloat(binding.ivTicketFront, "translationX", 1000f, 0f)
            animatorFront.duration = 1000
            animatorFront.start()

            delay(2000)
            view?.let {
                Navigation.findNavController(it).navigate(R.id.onboardingFragment)
            }
        }
    }

    private fun initObserver() {
        // 추후 자동로그인 가능 여부 판별 이후 로그인 페이지 또는 메인페이지로 이동
    }
}