package org.sfy.ttrip.presentation.init

import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentLoginBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override fun initView() {
        initListener()
    }

    private fun initListener() {
        binding.apply {
            ivBackToOnboarding.setOnClickListener {
                popBackStack()
            }

            tvLogin.setOnClickListener {
                // 추후 로그인 이후 회원 정보 입력 api 연결 시 수정 예정
                //navigate(LoginFragmentDirections.actionLoginFragmentToSignUpInformationFragment())
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}