package org.sfy.ttrip.presentation.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.common.util.dateFormat
import org.sfy.ttrip.databinding.ItemChatBinding
import org.sfy.ttrip.domain.entity.chat.ChatDetail

class ChatDetailAdapter(var items: List<ChatDetail>) : RecyclerView.Adapter<ChatDetailAdapter.ChatDetailViewHolder>() {

    lateinit var binding: ItemChatBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatDetailViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_chat,
            parent,
            false
        )
        return ChatDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatDetailViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ChatDetailViewHolder(
        private val binding: ItemChatBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ChatDetail) {
            if (data.isMine) {
                binding.apply {
                    clOtherMessage.visibility = View.GONE
                    clMyMessage.visibility = View.VISIBLE
                    tvMyMessage.text = data.content
                    tvMyTime.text = dateFormat(data.createdDate)
                }
            } else {
                binding.apply {
                    clMyMessage.visibility = View.GONE
                    clOtherMessage.visibility = View.VISIBLE
                    tvOtherMessage.text = data.content
                    tvOtherTime.text = dateFormat(data.createdDate)
                }
            }
        }
    }

    fun setChat(chatDetail: List<ChatDetail>) {
        this.items = chatDetail
        notifyDataSetChanged()
    }
}