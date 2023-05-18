package org.sfy.ttrip.presentation.board

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentPostBoardBinding
import org.sfy.ttrip.presentation.base.BaseFragment

class PostBoardFragment : BaseFragment<FragmentPostBoardBinding>(R.layout.fragment_post_board) {

    private val viewModel by activityViewModels<BoardViewModel>()
    private var keyboardVisible = false
    private lateinit var callback: OnBackPressedCallback
    private var waitTime = 0L

    override fun initView() {
        initListener()
        initContent()
        (activity as MainActivity).hideBottomNavigation(true)
        viewModel.clearPostData()
        initObserve()

        // 등록
        val keyboardVisibilityEventListener = object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                // 키보드 가시성 변경 시 호출되는 콜백 메서드
                if (isOpen) {
                    // 키보드가 올라왔을 때 처리
                    binding.tvNextPost.visibility = View.GONE
                } else {
                    // 키보드가 내려갔을 때 처리
                    binding.tvNextPost.visibility = View.VISIBLE
                }
            }
        }

        KeyboardVisibilityEvent.setEventListener(requireActivity(), keyboardVisibilityEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).hideBottomNavigation(false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - waitTime >= 2500) {
                    waitTime = System.currentTimeMillis()
                    showToast("뒤로가기 버튼을\n한번 더 누르면 작성 취소됩니다.")
                } else {
                    popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    // 키보드 가시성 상태 확인
    private fun isKeyboardVisible(): Boolean {
        return keyboardVisible
    }

    private fun initListener() {
        binding.ivBackToBoard.setOnClickListener {
            popBackStack()
        }
    }

    private fun initObserve(){
        // 옵저빙하고 나서 진행 해야함
        viewModel.boardId.observe(requireActivity()) {
            if (it != null) {
                navigate(PostBoardFragmentDirections.actionPostBoardFragmentToFinishPostBoardFragment())
            }
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
                            viewModel.postBoardNation.observe(this@PostBoardFragment) {
                                if (it == null || it == "") checkInfo(false)
                                else {
                                    viewModel.postBoardCity.observe(this@PostBoardFragment) {
                                        if (it == null || it == "") checkInfo(false)
                                        else checkInfo(true)
                                    }
                                }
                            }
                        }
                        3 -> {
                            binding.tvNextPost.text = "다음"
                            checkInfo(true)
                        }
                        4 -> {
                            binding.tvNextPost.text = "등록"
                            viewModel.postEndDate.observe(this@PostBoardFragment) {
                                if (it == null || it == "") checkInfo(false)
                                else {
                                    checkInfo(true)
                                    binding.tvNextPost.apply {
                                        setOnClickListener {
                                            showToast("게시글이 작성되었습니다")
                                            viewModel.postBoard()
                                        }
                                    }
                                }
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