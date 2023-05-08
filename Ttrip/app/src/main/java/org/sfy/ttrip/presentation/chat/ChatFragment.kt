package org.sfy.ttrip.presentation.chat

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

    private val chatViewModel by activityViewModels<ChatViewModel>()
    private val chatRoomAdapter by lazy { ChatRoomAdapter(this::getChatDetail, this::exitChatRoom) }
    private var chatId = 0

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(false)
        initRecyclerView()
        setChatRooms()
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
        isMatch: Boolean
    ) {
        navigate(
            ChatFragmentDirections.actionChatFragmentToChatDetailFragment(
                chatId,
                memberId,
                imagePath,
                articleTitle,
                nickname,
                articleId,
                isMatch
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