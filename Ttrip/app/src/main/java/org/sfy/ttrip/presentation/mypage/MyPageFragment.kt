package org.sfy.ttrip.presentation.mypage

import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentMypageBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(true)
        initListener()
    }

    private fun initListener() {
        binding.ivEditProfile.setOnClickListener {
            showToast("이동")
            navigate(MyPageFragmentDirections.actionMyPageFragmentToEditProfileFragment())
        }
    }
}