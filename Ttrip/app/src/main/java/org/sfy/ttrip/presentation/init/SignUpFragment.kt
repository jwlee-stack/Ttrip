package org.sfy.ttrip.presentation.init

import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentSignUpBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    override fun initView() {
        initListener()
    }

    private fun initListener() {
        binding.ivBackToOnboarding.setOnClickListener {
            popBackStack()
        }
    }
}