package org.sfy.ttrip.presentation.board

import android.content.Context
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentBoardBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class BoardFragment : BaseFragment<FragmentBoardBinding>(R.layout.fragment_board) {

    private lateinit var callback: OnBackPressedCallback
    private var waitTime = 0L

    private val viewModel by activityViewModels<BoardViewModel>()
    private val boardListAdapter by lazy {
        BoardListAdapter(this::selectBoard, requireContext())
    }

    override fun initView() {
        initObserver()
        initRecyclerView()
        initListener()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - waitTime >= 2500) {
                    waitTime = System.currentTimeMillis()
                    showToast("뒤로가기 버튼을\n한번 더 누르면 종료됩니다.")
                } else {
                    requireActivity().finishAffinity()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun initListener() {
        binding.apply {
            ivSearchBoard.setOnClickListener {
                if (etSearchBoard.visibility == View.INVISIBLE) {
                    etSearchBoard.visibility = View.VISIBLE
                } else {
                    viewModel.getBoards(2, "", "", etSearchBoard.text.toString())
                    etSearchBoard.visibility = View.INVISIBLE
                }
            }
            ivPostBoard.setOnClickListener {
                navigate(BoardFragmentDirections.actionBoardFragmentToPostBoardFragment())
            }
        }
    }

    private fun initRecyclerView() {
        viewModel.getBoards(0, "", "", "")
        binding.rvBoardBrief.apply {
            adapter = boardListAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun initObserver() {
        viewModel.boardListData.observe(viewLifecycleOwner) { response ->
            response?.let { boardListAdapter.setBoard(it) }
        }
    }

    private fun selectBoard(boardId: Int, dDay: Int) {
        navigate(BoardFragmentDirections.actionBoardFragmentToBoardDetailFragment(boardId, dDay))
    }
}