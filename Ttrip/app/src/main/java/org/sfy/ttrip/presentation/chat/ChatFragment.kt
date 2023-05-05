package org.sfy.ttrip.presentation.chat

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentChatBinding
import org.sfy.ttrip.presentation.base.BaseFragment

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat) {

    private val chatViewModel by activityViewModels<ChatViewModel>()
    private val chatRoomAdapter by lazy { ChatRoomAdapter(this::getChatDetail) }

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
        articleId: Int
    ) {
        navigate(
            ChatFragmentDirections.actionChatFragmentToChatDetailFragment(
                chatId,
                memberId,
                imagePath,
                articleTitle,
                nickname,
                articleId
            )
        )
    }
}