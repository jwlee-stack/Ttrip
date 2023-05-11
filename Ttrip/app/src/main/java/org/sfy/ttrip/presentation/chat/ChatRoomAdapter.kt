package org.sfy.ttrip.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.common.util.dateFormat
import org.sfy.ttrip.databinding.ItemChatRoomBinding
import org.sfy.ttrip.domain.entity.chat.ChatRoom

class ChatRoomAdapter(
    private val onChatRoomClicked: (
        chatId: Int,
        memberId: String,
        imagePath: String?,
        articleTitle: String,
        nickname: String,
        articleId: Int,
        isMatch: Boolean,
        status: String
    ) -> Unit,
    private val onChatRoomLongClicked: (chatId: Int) -> Unit
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
        return ChatRoomViewHolder(binding, onChatRoomClicked, onChatRoomLongClicked)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ChatRoomViewHolder(
        private val binding: ItemChatRoomBinding,
        private val onChatRoomClicked: (
            chatId: Int,
            memberId: String,
            imagePath: String?,
            articleTitle: String,
            nickname: String,
            articleId: Int,
            isMatch: Boolean,
            status: String
        ) -> Unit,
        private val onChatRoomLongClicked: (chatId: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ChatRoom) {
            binding.apply {
                binding.chat = data
                binding.tvChatTime.text = dateFormat(data.updatedAt)
                root.setOnClickListener {
                    onChatRoomClicked(
                        data.chatId,
                        data.otherUuid,
                        data.imagePath,
                        data.articleTitle,
                        data.otherNickname,
                        data.articleId,
                        data.isMatch,
                        data.status
                    )
                }
                root.setOnLongClickListener {
                    onChatRoomLongClicked(data.chatId)
                    true
                }
                if (data.similarity <= 50) {
                    tvChatSimilarity.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.lochmara
                        )
                    )
                    clChatRoom.setBackgroundResource(R.drawable.bg_rect_lochmara2_alice_blue2_radius10_stroke1)
                } else if (data.similarity <= 80) {
                    tvChatSimilarity.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.limerick
                        )
                    )
                    clChatRoom.setBackgroundResource(R.drawable.bg_rect_limerick_twilight_blue_radius10_stroke1)
                } else {
                    tvChatSimilarity.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.medium_orchid
                        )
                    )
                    clChatRoom.setBackgroundResource(R.drawable.bg_rect_medium_orchid_white_lilac_radius10_stroke1)
                }
            }
        }
    }

    fun setChatRooms(chatRoom: List<ChatRoom>) {
        this.items = chatRoom
        notifyDataSetChanged()
    }
}