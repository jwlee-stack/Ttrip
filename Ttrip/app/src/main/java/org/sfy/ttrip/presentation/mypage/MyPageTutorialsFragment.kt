package org.sfy.ttrip.presentation.mypage

import androidx.viewpager2.widget.ViewPager2
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentTutorialsBinding
import org.sfy.ttrip.presentation.base.BaseFragment

class MyPageTutorialsFragment :
    BaseFragment<FragmentTutorialsBinding>(R.layout.fragment_tutorials) {

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(true)
        initContent()
    }

    private fun initContent() {
        binding.apply {
            vpContentTutorials.adapter = TutorialsAdapter(this@MyPageTutorialsFragment)

            vpContentTutorials.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                }
            })
        }
    }
}