package org.sfy.ttrip.presentation.init

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return OnBoardingBannerFragment().apply {
            arguments = Bundle().apply {
                putInt("banner_position", position)
            }
        }
    }
}