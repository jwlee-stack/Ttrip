package org.sfy.ttrip.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.ItemChatRoomBinding
import org.sfy.ttrip.domain.entity.chat.ChatRoom

class ChatRoomAdapter(
    private val onChatRoomClicked: (chatId: Int) -> Unit
) : RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {

    private var items: List<ChatRoom> = listOf()
    lateinit var binding: ItemChatRoomBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_chat_room,
            parent,
            false
        )
        return ChatRoomViewHolder(binding, onChatRoomClicked)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ChatRoomViewHolder(
        private val binding: ItemChatRoomBinding,
        private val onChatRoomClicked: (chatId: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ChatRoom) {
            binding.apply {
                root.setOnClickListener {
                    onChatRoomClicked(data.chatId)
                }
            }
        }
    }

    fun setChatRooms(chatRoom: List<ChatRoom>) {
        this.items = chatRoom
        notifyDataSetChanged()
    }
}