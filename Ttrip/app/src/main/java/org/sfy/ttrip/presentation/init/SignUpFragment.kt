package org.sfy.ttrip.presentation.init

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentSignUpBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val singUpViewModel by activityViewModels<AuthViewModel>()
    private val mAuth = FirebaseAuth.getInstance()
    private var verificationCode = ""

    override fun initView() {
        initListener()
        passwordMatchCheck()
        observeData()
        passwordValidCheck()
    }

    private fun observeData() {
        singUpViewModel.passwordMatch.observe(viewLifecycleOwner) { passwd ->
            singUpViewModel.verifyPhoneSuccess.observe(viewLifecycleOwner) { phone ->
                if (passwd && phone) {
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
    }

    private fun initListener() {
        binding.apply {
            ivBackToOnboarding.setOnClickListener {
                popBackStack()
            }
            tvVerifyPhone.setOnClickListener {
                sendCertificationCode()
            }
            tvVerifyCertificationNumber.setOnClickListener {
                if (etSignUpPhoneCheck.text.toString().isNotEmpty()) {
                    val credential = PhoneAuthProvider.getCredential(
                        verificationCode,
                        etSignUpPhoneCheck.text.toString()
                    )
                    signInWithPhoneAuthCredential(credential)
                }
            }
            tvVerificationAgain.setOnClickListener {
                binding.apply {
                    tvVerifyPhone.setBackgroundResource(R.drawable.bg_rect_neon_blue_radius40)
                    tvVerifyPhone.isEnabled = true
                    etSignUpPhone.isEnabled = true
                    tvVerificationAgain.visibility = View.GONE
                }
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

    private fun sendCertificationCode() {
        val phoneNumber = "+82" + binding.etSignUpPhone.text.substring(1)
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    showToast("인증번호가 전송되었습니다.")
                    verificationCode = verificationId
                    binding.apply {
                        tvVerifyPhone.setBackgroundResource(R.drawable.bg_rect_grey_radius40)
                        tvVerifyPhone.isEnabled = false
                        etSignUpPhone.isEnabled = false
                        tvVerificationAgain.visibility = View.VISIBLE
                    }
                }

                override fun onVerificationCompleted(phoneAuth: PhoneAuthCredential) {}
                override fun onVerificationFailed(e: FirebaseException) {}
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        mAuth.setLanguageCode("kr")
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    showToast("인증되었습니다!")
                    binding.apply {
                        tvVerifyCertificationNumber.setBackgroundResource(R.drawable.bg_rect_grey_radius40)
                        tvVerifyCertificationNumber.isEnabled = false
                        etSignUpPhoneCheck.isEnabled = false
                        tvVerificationAgain.visibility = View.GONE
                        singUpViewModel.verifyPhoneSuccess.value = true
                    }
                } else {
                    showToast("인증 실패했습니다. 다시 시도해주세요.")
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