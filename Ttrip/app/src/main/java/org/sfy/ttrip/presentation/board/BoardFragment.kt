package org.sfy.ttrip.presentation.board

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentBoardBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class BoardFragment : BaseFragment<FragmentBoardBinding>(R.layout.fragment_board) {

    override fun initView() {
        initListener()
    }

    private fun initListener() {
        binding.apply {
            ivSearchBoard.setOnClickListener {
                if (etSearchBoard.visibility == View.INVISIBLE) {
                    etSearchBoard.visibility = View.VISIBLE
                } else {
                    etSearchBoard.visibility = View.INVISIBLE
                }
            }
        }
    }

}