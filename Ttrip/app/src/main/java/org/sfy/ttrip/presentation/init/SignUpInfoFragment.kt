package org.sfy.ttrip.presentation.init

import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentSignUpInfoBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import org.sfy.ttrip.presentation.mypage.MyPageViewModel

@AndroidEntryPoint
class SignUpInfoFragment :
    BaseFragment<FragmentSignUpInfoBinding>(R.layout.fragment_sign_up_info) {

    private val viewModel by activityViewModels<UserInfoViewModel>()
    private val myPageViewModel by activityViewModels<MyPageViewModel>()
    override fun initView() {
        initContent()
        initListener()
    }

    private fun initListener() {
        binding.ivBackToLogin.setOnClickListener {
            myPageViewModel.logout()
            popBackStack()
        }

        // 등록
        val keyboardVisibilityEventListener = object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                // 키보드 가시성 변경 시 호출되는 콜백 메서드
                if (isOpen) {
                    // 키보드가 올라왔을 때 처리
                    binding.tvNextInfo.visibility = View.GONE
                } else {
                    // 키보드가 내려갔을 때 처리
                    binding.tvNextInfo.visibility = View.VISIBLE
                }
            }
        }

        KeyboardVisibilityEvent.setEventListener(requireActivity(), keyboardVisibilityEventListener)
    }

    private fun initContent() {
        binding.apply {
            vpContentInfo.adapter = SignUpAdapter(this@SignUpInfoFragment)
            ciBannerInfo.setViewPager(vpContentInfo)

            vpContentInfo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    vpContentInfo.isUserInputEnabled = false

                    when (position) {
                        0 -> {
                            viewModel.isDuplicate.observe(this@SignUpInfoFragment) {
                                it?.let {
                                    if (it) {
                                        checkInfo(false)
                                    } else {
                                        checkInfo(true)
                                    }
                                }
                            }
                        }
                        1 -> {
                            viewModel.userAge.observe(this@SignUpInfoFragment) {
                                if (it == "") checkInfo(false)
                                else checkInfo(true)
                            }
                        }
                        2 -> {
                            //viewModel.nickNameValid.removeObservers(this@SignUpInfoFragment)
                            viewModel.userSex.observe(this@SignUpInfoFragment) {
                                if (it == "") checkInfo(false)
                                else checkInfo(true)
                            }
                        }
                        3 -> {
                            binding.tvNextInfo.text = "다음"
                            viewModel.profileImgUri.observe(this@SignUpInfoFragment) { uri ->
                                if (uri == null) {
                                    checkInfo(false)
                                } else {
                                    viewModel.userIntro.observe(this@SignUpInfoFragment) {
                                        if (it == "") checkInfo(false)
                                        else checkInfo(true)
                                    }
                                }
                            }
                        }
                        4 -> {
                            binding.tvNextInfo.text = "입력 완료"
                            viewModel.userTest.observe(this@SignUpInfoFragment) {
                                if (it.preferCheapHotelThanComfort != 0 &&
                                    it.preferCheapTraffic != 0 &&
                                    it.preferGoodFood != 0 &&
                                    it.preferDirectFlight != 0 &&
                                    it.preferPlan != 0 &&
                                    it.preferTightSchedule != 0 &&
                                    it.preferShoppingThanTour != 0 &&
                                    it.preferNatureThanCity != 0 &&
                                    it.preferPersonalBudget != 0
                                ) {
                                    binding.tvNextInfo.apply {
                                        isEnabled = true
                                        setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
                                        setOnClickListener {
                                            viewModel.apply {
                                                postUserInfo()
                                                postUserInfoTest()
                                            }
                                            showToast("수고하셨습니다.")
                                            val intent =
                                                Intent(requireContext(), MainActivity::class.java)
                                            startActivity(intent)
                                        }
                                    }
                                } else {
                                    binding.tvNextInfo.apply {
                                        isEnabled = false
                                        setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun checkInfo(valid: Boolean?) {
        if (valid != null) {
            if (valid) {
                binding.apply {
                    vpContentInfo.isUserInputEnabled = true
                    tvNextInfo.apply {
                        setOnClickListener {
                            vpContentInfo.currentItem = vpContentInfo.currentItem + 1
                        }
                        isEnabled = true
                        setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
                        setTextColor(Color.BLACK)
                    }
                }
            } else {
                binding.apply {
                    vpContentInfo.isUserInputEnabled = false
                    tvNextInfo.apply {
                        isEnabled = false
                        setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                        setTextColor(Color.WHITE)
                    }
                }
            }
        } else {
            binding.apply {
                vpContentInfo.isUserInputEnabled = false
                tvNextInfo.apply {
                    isEnabled = false
                    setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                    setTextColor(Color.WHITE)
                }
            }
        }
    }
}