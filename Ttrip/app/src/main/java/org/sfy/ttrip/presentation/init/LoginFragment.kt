package org.sfy.ttrip.presentation.init

import android.content.Intent
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentLoginBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val authViewModel by viewModels<AuthViewModel>()

    override fun initView() {
        observeData()
        initListener()
    }

    private fun initListener() {
        binding.apply {
            ivBackToOnboarding.setOnClickListener {
                popBackStack()
            }

            tvLogin.setOnClickListener {
                val id = etLoginId.text.toString()
                val pw = etLoginPassword.text.toString()
                if (id.isEmpty() || pw.isEmpty()) {
                    showToast("모든 정보를 입력해주세요.")
                } else {
                    authViewModel.requestLogin(id, pw)
                }
            }
        }
    }

    private fun observeData() {
        authViewModel.emptyNickname.observe(viewLifecycleOwner) {
            if (it == true) {
                showToast("로그인되었습니다.")
                //navigate(LoginFragmentDirections.actionLoginFragmentToSignUpInformationFragment())
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
                showToast("돌아오신걸 환영해요!")
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}