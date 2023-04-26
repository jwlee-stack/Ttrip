package org.sfy.ttrip.presentation.init

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SignUpAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return SignUpInformationBannerFragment().apply {
            arguments = Bundle().apply {
                putInt("banner_position", position)
            }
        }
    }
}