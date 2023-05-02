package org.sfy.ttrip.presentation.mypage

import android.content.Intent
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentMypageBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import org.sfy.ttrip.presentation.init.InitActivity

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage),
    LogoutDialogListener {

    private val myPageViewModel by activityViewModels<MyPageViewModel>()

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(false)
        initListener()
        setUserProfile()
        myPageViewModel.getUserProfile()
    }

    override fun onConfirmButtonClicked() {
        myPageViewModel.logout()
        val intent = Intent(activity, InitActivity::class.java)
        startActivity(intent)
        showToast("로그아웃되었습니다.")
    }

    private fun initListener() {
        binding.apply {
            ivEditProfile.setOnClickListener {
                navigate(MyPageFragmentDirections.actionMyPageFragmentToEditProfileFragment())
            }
            tvTestAgain.setOnClickListener {
                navigate(MyPageFragmentDirections.actionMyPageFragmentToPreferenceTestAgainFragment())
            }
        }
        binding.tvLogout.setOnClickListener {
            val dialog = LogoutDialog(requireContext(), this)
            dialog.show()
        }
    }

    private fun setUserProfile() {
        myPageViewModel.userProfile.observe(viewLifecycleOwner) { response ->
            response?.let {
                binding.userProfile = it
                myPageViewModel.postNickname(it.nickname)
                myPageViewModel.postAge(it.age.toString())
            }
        }
        myPageViewModel.getUserProfile()
    }
}