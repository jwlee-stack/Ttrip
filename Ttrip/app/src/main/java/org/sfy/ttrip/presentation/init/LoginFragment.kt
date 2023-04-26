package org.sfy.ttrip.presentation.init

import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentLoginBinding
import org.sfy.ttrip.presentation.base.BaseFragment

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override fun initView() {
        initListener()
    }

    private fun initListener() {
        binding.apply {
            ivBackToOnboarding.setOnClickListener {
                popBackStack()
            }
        }
    }
}