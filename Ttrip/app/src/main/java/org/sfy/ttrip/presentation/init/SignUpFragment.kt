package org.sfy.ttrip.presentation.init

import android.view.View
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

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val mAuth = FirebaseAuth.getInstance()
    private var verificationCode = ""

    override fun initView() {
        initListener()
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
                val credential = PhoneAuthProvider.getCredential(
                    verificationCode,
                    etSignUpPhoneCheck.text.toString()
                )
                signInWithPhoneAuthCredential(credential)
            }
            tvVerificationAgain.setOnClickListener {
                binding.apply {
                    tvVerifyPhone.setBackgroundResource(R.drawable.bg_rect_neon_blue_radius40)
                    tvVerifyPhone.isEnabled = true
                    etSignUpPhone.isEnabled = true
                    tvVerificationAgain.visibility = View.GONE
                }
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
                    }
                } else {
                    showToast("인증 실패했습니다. 다시 시도해주세요.")
                }
            }
    }
}