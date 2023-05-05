package org.sfy.ttrip.presentation.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.sfy.ttrip.presentation.init.SignUpInfoContentFragment

class PostBoardAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return PostBoardContentFragment().apply {
            arguments = Bundle().apply {
                putInt("content_position", position)
            }
        }
    }
}