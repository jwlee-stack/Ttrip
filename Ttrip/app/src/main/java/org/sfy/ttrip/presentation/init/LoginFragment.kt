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
        authViewModel.clearEmptyNickname()
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
            if (authViewModel.isFreeze.value == false) {
                if (it == true) {
                    showToast("로그인되었습니다.")
                    navigate(LoginFragmentDirections.actionLoginFragmentToSignUpInformationFragment())
                } else if (it == false) {
                    showToast("돌아오신걸 환영해요!")
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            } else {
                // 관리자 메일 추가 필요
                showToast("신고된 계정입니다\n이메일을 통해 소명서를 제출해주세요")
            }
        }

        authViewModel.isValid.observe(viewLifecycleOwner) {
            if (!it) {
                showToast("번호 또는 비밀번호를 확인해보세요.")
                authViewModel.makeIsValidTrue()
            }
        }
    }
}