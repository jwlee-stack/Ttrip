package org.sfy.ttrip.presentation.board

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.common.util.BindingAdapters.setProfileImgString
import org.sfy.ttrip.databinding.FragmentBoardDetailBinding
import org.sfy.ttrip.presentation.base.BaseFragment

class BoardDetailFragment :
    BaseFragment<FragmentBoardDetailBinding>(R.layout.fragment_board_detail) {

    private val args by navArgs<BoardDetailFragmentArgs>()
    private val viewModel by activityViewModels<BoardViewModel>()

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(true)
        getData()
        initListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).hideBottomNavigation(false)
    }

    private fun initListener() {
        binding.apply {
            tvFinishBoard.setOnClickListener {
                // 모집 종료 예정
            }

            tvPostBoardComment.setOnClickListener {
                // 신청 예정
            }

            ivDeleteOption.setOnClickListener {

            }

            ivBackToBoard.setOnClickListener {
                popBackStack()
                (activity as MainActivity).hideBottomNavigation(false)
            }
        }
    }

    private fun getData() {
        viewModel.boardData.observe(this@BoardDetailFragment) {
            binding.apply {
                boardDetail = it
                ivBoardDetailUserProfile.setProfileImgString(it!!.imgPath)

                // 본인 게시물
                if (it.isMine) {
                    tvPostBoardComment.visibility = View.GONE
                    tvFinishBoard.visibility = View.VISIBLE

                    tvUserSimilarity.visibility = View.GONE
                    ivArrowBoardDetailUserDetail.visibility = View.GONE
                    ivDeleteOption.visibility = View.VISIBLE

                    changeVisibility(tvFinishBoard, it.status.toString() == "T")
                } else {
                    // 타인 게시물
                    tvPostBoardComment.visibility = View.VISIBLE
                    tvFinishBoard.visibility = View.GONE

                    tvUserSimilarity.visibility = View.VISIBLE
                    ivArrowBoardDetailUserDetail.visibility = View.VISIBLE
                    ivDeleteOption.visibility = View.GONE

                    // 모집 진행중인 경우
                    if (it.status.toString() == "T") {
                        // 이미 신청한 경우
                        if (it.isApplied) {
                            changeVisibility(tvPostBoardComment, false)
                        } else {
                            changeVisibility(tvPostBoardComment, it.status.toString() == "T")
                        }
                    } else {
                        changeVisibility(tvPostBoardComment, false)
                    }
                }

                tvBoardDetailContent.text = it.content
                tvDetailDuring.text = "${it.startDate} ~ ${it.endDate}"
                tvDetailNationCity.text = "${it.nation} - ${it.city}"

                val inputDateTime = it.createdDate
                val date = inputDateTime.substringBefore("T")
                val time = inputDateTime.substringAfter("T").substringBeforeLast(":")
                tvBoardDetailUserDate.text = "${date.replace("-", ".")} $time"

                tvUserSimilarity.apply {
                    if (it.similarity <= 50) {
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.lochmara2))
                        setBackgroundResource(R.drawable.bg_rect_lochmara2_alice_blue2_radius10_stroke1)
                    } else if (it.similarity <= 80) {
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.limerick))
                        setBackgroundResource(R.drawable.bg_rect_limerick_twilight_blue_radius10_stroke1)
                    } else {
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.medium_orchid
                            )
                        )
                        setBackgroundResource(R.drawable.bg_rect_wisteria_white_lilac_radius10_stroke1)
                    }
                    text = "${it.similarity}%"
                }
            }
        }
        viewModel.getBoardDetail(args.boardId)
    }

    private fun changeVisibility(textView: TextView, boolean: Boolean) {
        when (boolean) {
            true -> {
                textView.apply {
                    isEnabled = true
                    setBackgroundResource(R.drawable.bg_rect_pear_radius10)
                }
            }
            false -> {
                textView.apply {
                    isEnabled = false
                    setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                }
            }
        }
    }
}