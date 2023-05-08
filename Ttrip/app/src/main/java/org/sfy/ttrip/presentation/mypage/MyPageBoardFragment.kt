package org.sfy.ttrip.presentation.mypage

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentMyPageBoardBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class MyPageBoardFragment :
    BaseFragment<FragmentMyPageBoardBinding>(R.layout.fragment_my_page_board) {

    private val myPageViewModel by activityViewModels<MyPageViewModel>()
    private val myPostsAdapter by lazy {
        MyPostsAdapter(this::selectBoard, requireContext())
    }

    override fun initView() {
        initRecyclerView()
        getMyPosts()
    }

    private fun initRecyclerView() {
        binding.rvMyPost.apply {
            adapter = myPostsAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun getMyPosts() {
        myPageViewModel.posts.observe(viewLifecycleOwner) { response ->
            response?.let { myPostsAdapter.setBoard(it) }
        }
        myPageViewModel.getMyPosts()
    }

    private fun selectBoard(boardId: Int, dDay: Int) {
        navigate(
            MyPageBoardFragmentDirections.actionMyPageBoardFragmentToBoardDetailFragment(
                boardId,
                dDay
            )
        )
    }
}