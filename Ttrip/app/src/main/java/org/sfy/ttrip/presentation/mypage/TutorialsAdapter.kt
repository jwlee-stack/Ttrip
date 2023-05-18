package org.sfy.ttrip.presentation.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TutorialsAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return MyPageTutorialsContentFragment().apply {
            arguments = Bundle().apply {
                putInt("content_tutorials", position)
            }
        }
    }
}