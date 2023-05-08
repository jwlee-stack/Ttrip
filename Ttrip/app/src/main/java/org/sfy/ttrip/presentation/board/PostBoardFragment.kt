package org.sfy.ttrip.presentation.board

import android.graphics.Color
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentPostBoardBinding
import org.sfy.ttrip.presentation.base.BaseFragment

class PostBoardFragment : BaseFragment<FragmentPostBoardBinding>(R.layout.fragment_post_board) {

    private val viewModel by activityViewModels<BoardViewModel>()

    override fun initView() {
        initListener()
        initContent()
        (activity as MainActivity).hideBottomNavigation(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).hideBottomNavigation(false)
    }

    private fun initListener() {
        binding.ivBackToBoard.setOnClickListener {
            popBackStack()
        }
    }

    private fun initContent() {
        binding.apply {
            vpContentPostBoard.adapter = PostBoardAdapter(this@PostBoardFragment)
            ciContentPostBoard.setViewPager(vpContentPostBoard)

            vpContentPostBoard.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    vpContentPostBoard.isUserInputEnabled = false

                    when (position) {
                        0 -> {
                            viewModel.postBoardTitle.observe(this@PostBoardFragment) {
                                if (it == null || it == "") checkInfo(false)
                                else checkInfo(true)
                            }
                        }
                        1 -> {
                            viewModel.postBoardContent.observe(this@PostBoardFragment) {
                                if (it == null || it == "") checkInfo(false)
                                else checkInfo(true)
                            }
                        }
                        2 -> {
                            checkInfo(true)
                        }
                        3 -> {
                            binding.tvNextPost.text = "다음"
                            checkInfo(true)
                        }
                        4 -> {
                            checkInfo(true)
                            binding.tvNextPost.apply {
                                text = "등록"
                                setOnClickListener {
                                    viewModel.postBoard()
                                }
                                popBackStack()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun checkInfo(valid: Boolean?) {
        if (valid != null) {
            if (valid) {
                binding.apply {
                    vpContentPostBoard.isUserInputEnabled = true
                    tvNextPost.apply {
                        setOnClickListener {
                            vpContentPostBoard.currentItem = vpContentPostBoard.currentItem + 1
                        }
                        isEnabled = true
                        setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
                        setTextColor(Color.BLACK)
                    }
                }
            } else {
                binding.apply {
                    vpContentPostBoard.isUserInputEnabled = false
                    tvNextPost.apply {
                        isEnabled = false
                        setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                        setTextColor(Color.WHITE)
                    }
                }
            }
        } else {
            binding.apply {
                vpContentPostBoard.isUserInputEnabled = false
                tvNextPost.apply {
                    isEnabled = false
                    setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                    setTextColor(Color.WHITE)
                }
            }
        }
    }
}