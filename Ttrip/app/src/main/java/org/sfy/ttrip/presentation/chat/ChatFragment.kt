package org.sfy.ttrip.presentation.chat

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentChatBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat),
    ExitChatDialogListener {

    private lateinit var callback: OnBackPressedCallback
    private var waitTime = 0L

    private val chatViewModel by activityViewModels<ChatViewModel>()
    private val chatRoomAdapter by lazy { ChatRoomAdapter(this::getChatDetail, this::exitChatRoom) }
    private var chatId = 0

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(false)
        initRecyclerView()
        setChatRooms()
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

    private fun initRecyclerView() {
        binding.rvChatRooms.apply {
            adapter = chatRoomAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun setChatRooms() {
        chatViewModel.chatRooms.observe(viewLifecycleOwner) { response ->
            response?.let { chatRoomAdapter.setChatRooms(it) }
        }
        chatViewModel.getChatRooms()
    }

    private fun getChatDetail(
        chatId: Int,
        memberId: String,
        imagePath: String?,
        articleTitle: String,
        nickname: String,
        articleId: Int,
        isMatch: Boolean,
        status: String
    ) {
        navigate(
            ChatFragmentDirections.actionChatFragmentToChatDetailFragment(
                chatId,
                memberId,
                imagePath,
                articleTitle,
                nickname,
                articleId,
                isMatch,
                status
            )
        )
    }

    private fun exitChatRoom(chatId: Int) {
        this.chatId = chatId
        val dialog = ExitChatDialog(requireContext(), this)
        dialog.show()
    }

    override fun onConfirmButtonClicked() {
        lifecycleScope.launch {
            chatViewModel.exitChatRoom(chatId)
            delay(300)
            showToast("삭제되었습니다.")
            chatViewModel.getChatRooms()
        }
    }
}