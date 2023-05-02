package org.sfy.ttrip.presentation.mypage

import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentMypageBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    private val myPageViewModel by activityViewModels<MyPageViewModel>()

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(false)
        initListener()
        setUserProfile()
        myPageViewModel.getUserProfile()
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