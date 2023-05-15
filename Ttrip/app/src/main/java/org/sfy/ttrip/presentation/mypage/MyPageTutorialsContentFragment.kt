package org.sfy.ttrip.presentation.mypage

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentTutorialsContentBinding
import org.sfy.ttrip.presentation.base.BaseFragment

class MyPageTutorialsContentFragment :
    BaseFragment<FragmentTutorialsContentBinding>(R.layout.fragment_tutorials_content) {

    private var bannerPosition = -1

    override fun initView() {
        bannerPosition = arguments?.getInt("content_tutorials")!!
        with(bannerPosition) {
            val contentData = listOf(
                binding.clTutorialsBoard,
                binding.clTutorialsBoardDetail,
                binding.clTutorialsLive,
                binding.clTutorialsAr,
                binding.clTutorialsChat,
                binding.clTutorialsProfile
            )

            when (bannerPosition) {
                0 -> changeVisibility(0, contentData)
                1 -> changeVisibility(1, contentData)
                2 -> changeVisibility(2, contentData)
                3 -> changeVisibility(3, contentData)
                4 -> changeVisibility(4, contentData)
                5 -> changeVisibility(5, contentData)
            }
        }
    }

    private fun changeVisibility(position: Int, bannerData: List<ConstraintLayout>) {
        for (i in 0..5) {
            bannerData[i].visibility = View.GONE
        }
        bannerData[position].visibility = View.VISIBLE
    }
}