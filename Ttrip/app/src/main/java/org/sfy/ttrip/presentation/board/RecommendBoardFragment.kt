package org.sfy.ttrip.presentation.board

import android.content.Context
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentRecommendBoardListBinding
import org.sfy.ttrip.presentation.base.BaseFragment

class RecommendBoardFragment :
    BaseFragment<FragmentRecommendBoardListBinding>(R.layout.fragment_recommend_board_list) {

    private lateinit var callback: OnBackPressedCallback

    private val viewModel by activityViewModels<BoardViewModel>()
    private val recommendBoardAdapter by lazy {
        RecommendBoardAdapter(this::selectBoard)
    }

    override fun initView() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(200)
            if (viewModel.recommendBoardListData.value!!.isEmpty()) {
                binding.apply {
                    vpRecommendBoard.visibility = View.GONE
                    tvNoneBoard.visibility = View.VISIBLE
                }

            } else {
                binding.apply {
                    vpRecommendBoard.visibility = View.VISIBLE
                    tvNoneBoard.visibility = View.GONE
                }
                recommendBoardAdapter.setRecommendBoard(viewModel.recommendBoardListData.value!!)
            }
            initViewPager()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigate(RecommendBoardFragmentDirections.actionRecommendBoardFragmentToBoardFragment())
                (activity as MainActivity).hideBottomNavigation(false)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun initViewPager() {
        val offsetBetweenPages =
            resources.getDimensionPixelOffset(R.dimen.offsetBetweenPages).toFloat()

        binding.ivBackToBoard.setOnClickListener {
            navigate(RecommendBoardFragmentDirections.actionRecommendBoardFragmentToBoardFragment())
            (activity as MainActivity).hideBottomNavigation(false)
        }

        binding.vpRecommendBoard.apply {
            adapter = recommendBoardAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 4
            setPageTransformer { page, position ->
                val myOffset = position * -(2 * offsetBetweenPages)
                if (position < -1) {
                    page.translationX = -myOffset
                } else if (position <= 1) {
                    val scaleFactor = 0.8f.coerceAtLeast(1 - kotlin.math.abs(position))
                    page.translationX = myOffset
                    page.scaleY = scaleFactor
                    page.alpha = scaleFactor
                } else {
                    page.alpha = 0f
                    page.translationX = myOffset
                }
            }
        }
    }

    private fun selectBoard(boardId: Int, dDay: Int) {
        navigate(
            RecommendBoardFragmentDirections.actionRecommendBoardFragmentToBoardDetailFragment(
                boardId,
                dDay
            )
        )
    }
}