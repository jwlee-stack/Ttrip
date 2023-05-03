package org.sfy.ttrip.presentation.board

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentBoardDetailBinding
import org.sfy.ttrip.presentation.base.BaseFragment

class BoardDetailFragment :
    BaseFragment<FragmentBoardDetailBinding>(R.layout.fragment_board_detail) {

    private val args by navArgs<BoardDetailFragmentArgs>()
    private val viewModel by activityViewModels<BoardViewModel>()

    override fun initView() {
        getData()
        initListener()
    }

    private fun initListener() {
        binding.apply {
            tvFinishBoard.setOnClickListener {
                showToast("모집 끝!")
            }
        }
    }

    private fun getData() {
        viewModel.boardData.observe(this@BoardDetailFragment) {
            binding.apply {
                boardDetail = it
                // 본인 게시물
                if (it!!.isMine) {
                    tvPostBoardComment.visibility = View.GONE
                    tvFinishBoard.visibility = View.VISIBLE

                    tvFinishBoard.apply {
                        isEnabled = if (it.status.toString() == "T") {
                            setBackgroundResource(R.drawable.bg_rect_pear_radius10)
                            true
                        } else {
                            setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                            false
                        }
                    }

                } else {
                    // 타인 게시물
                    tvPostBoardComment.visibility = View.VISIBLE
                    tvFinishBoard.visibility = View.GONE

                    // 모집 진행중인 경우
                    if (it.status.toString() == "T") {
                        // 이미 신청한 경우
                        if (it.isApplied) {
                            tvPostBoardComment.apply {
                                isEnabled = false
                                setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                            }
                        } else{
                            tvPostBoardComment.apply {
                                isEnabled = if (it.status.toString() == "T") {
                                    setBackgroundResource(R.drawable.bg_rect_pear_radius10)
                                    true
                                } else {
                                    setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                                    false
                                }
                            }
                        }
                    } else {
                        tvPostBoardComment.apply {
                            isEnabled = false
                            setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                        }
                    }
                }
                tvBoardDetailContent.text = it.content
                tvDetailDuring.text = "${it.startDate} ~ ${it.endDate}"
            }
        }
        viewModel.getBoardDetail(args.boardId)
    }
}