package org.sfy.ttrip.presentation.board

import androidx.fragment.app.activityViewModels
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentFinishPostBoardBinding
import org.sfy.ttrip.presentation.base.BaseFragment

class FinishPostBoardFragment :
    BaseFragment<FragmentFinishPostBoardBinding>(R.layout.fragment_finish_post_board) {

    private val viewModel by activityViewModels<BoardViewModel>()

    override fun initView() {
        initListener()
    }

    private fun initListener() {
        binding.apply {
            tvBackToBoard.setOnClickListener {

            }
            tvGoToRecommend.setOnClickListener {
                viewModel.recommendBoardListData.observe(this@FinishPostBoardFragment){
                    if (it != null){
                        //navigate()
                    }
                }
                viewModel.getRecommendBoard()
            }
        }
    }
}