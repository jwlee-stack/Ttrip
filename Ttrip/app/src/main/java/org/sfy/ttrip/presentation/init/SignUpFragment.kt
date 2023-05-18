package org.sfy.ttrip.presentation.init

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentSignUpBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import java.util.regex.Pattern

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val singUpViewModel by activityViewModels<AuthViewModel>()

    override fun initView() {
        initListener()
        passwordMatchCheck()
        observeData()
        passwordValidCheck()
    }

    private fun observeData() {
        singUpViewModel.passwordMatch.observe(viewLifecycleOwner) { passwd ->
            if (passwd) {
                binding.tvSignUp.apply {
                    setBackgroundResource(R.drawable.bg_rect_pear_radius10)
                    isEnabled = true
                }
            } else {
                binding.tvSignUp.apply {
                    setBackgroundResource(R.drawable.bg_rect_whisper_radius10)
                    isEnabled = false
                }
            }
        }
    }

    private fun initListener() {
        binding.apply {
            ivBackToOnboarding.setOnClickListener {
                popBackStack()
            }
            tvSignUp.setOnClickListener {
                singUpViewModel.requestSignUp(
                    etSignUpPhone.text.toString(),
                    etSignUpPassword.text.toString()
                )
                showToast("회원가입되었습니다!")
                navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
            }
        }
    }

    private fun passwordMatchCheck() {
        binding.apply {
            etSignUpPasswordCheck.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (etSignUpPassword.text.toString() == etSignUpPasswordCheck.text.toString()) {
                        tvNotMatchPassword.visibility = View.GONE
                        singUpViewModel.passwordMatch.value = true
                    } else {
                        tvNotMatchPassword.visibility = View.VISIBLE
                        singUpViewModel.passwordMatch.value = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (etSignUpPassword.text.toString() == etSignUpPasswordCheck.text.toString()) {
                        tvNotMatchPassword.visibility = View.GONE
                        singUpViewModel.passwordMatch.value = true
                    } else {
                        tvNotMatchPassword.visibility = View.VISIBLE
                        singUpViewModel.passwordMatch.value = false
                    }
                }
            })
        }
    }

    private fun passwordValidCheck() {
        binding.etSignUpPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!Pattern.matches(
                        "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$",
                        binding.etSignUpPassword.text.toString()
                    )
                ) {
                    showToast("8자 이상의 특수문자와 영문 조합의 \n비밀번호를 생성해주세요.")
                    binding.etSignUpPasswordCheck.isEnabled = false
                    return
                } else {
                    binding.etSignUpPasswordCheck.isEnabled = true
                }
            }
        })
    }
}