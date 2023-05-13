package org.sfy.ttrip.presentation.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
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

    // 초기값으로 초기화합니다.
    private var articleId: String? = null
    private var dDay: String? = null

    override fun initView() {
        initObserver()
        initRecyclerView()
        initListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 프래그먼트의 arguments에서 추가 데이터를 가져옵니다.
        //val articleId = arguments?.getString("articleId")
        //val dDay = arguments?.getString("dDay")
        // 여기서 extraData를 사용하여 필요한 작업을 수행합니다.

        articleId = arguments?.getString("articleId")
        dDay = arguments?.getString("dDay")
        Log.d("tpfla", "initView: $articleId")
        Log.d("tpfla", "initView: $dDay")

        if (MainActivity.NEW_ALARM_FLAG && arguments != null) {

            selectBoard(articleId!!.toInt(), dDay!!.toInt())
            MainActivity.NEW_ALARM_FLAG = false
        }
//        if (savedInstanceState != null) {
//            //selectBoard(articleId!!.toInt(), dDay!!.toInt())
//            articleId = savedInstanceState.getString("articleId")
//            dDay = savedInstanceState.getString("dDay")
//        } else {
//            // 프래그먼트의 arguments에서 데이터를 가져옵니다.
//            if (arguments != null) {
//                articleId = arguments?.getString("articleId")
//                dDay = arguments?.getString("dDay")
//                selectBoard(articleId!!.toInt(), dDay!!.toInt())
//            }
//        }


    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        // 현재 값을 저장합니다.
//        outState.putString("articleId", articleId)
//        outState.putString("dDay", dDay)
//    }

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