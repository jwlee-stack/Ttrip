package org.sfy.ttrip.presentation.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.common.util.convertDateString
import org.sfy.ttrip.common.util.detailDateFormat
import org.sfy.ttrip.databinding.ItemChatBinding
import org.sfy.ttrip.domain.entity.chat.ChatDetail

class ChatDetailAdapter(var items: List<ChatDetail>) :
    RecyclerView.Adapter<ChatDetailAdapter.ChatDetailViewHolder>() {

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
                binding.clDatelineOther.visibility = View.GONE
                if (data.isFirst) {
                    binding.tvDatelineMine.text = convertDateString(data.createdAt)
                    binding.clDatelineMine.visibility = View.VISIBLE
                } else {
                    binding.clDatelineMine.visibility = View.GONE
                }
                binding.apply {
                    clOtherMessage.visibility = View.GONE
                    clMyMessage.visibility = View.VISIBLE
                    tvMyMessage.text = data.content
                    tvMyTime.text = detailDateFormat(data.createdAt)
                }
            } else {
                binding.clDatelineMine.visibility = View.GONE
                if (data.isFirst) {
                    binding.tvDatelineOther.text = convertDateString(data.createdAt)
                    binding.clDatelineOther.visibility = View.VISIBLE
                } else {
                    binding.clDatelineOther.visibility = View.GONE
                }
                binding.apply {
                    clMyMessage.visibility = View.GONE
                    clOtherMessage.visibility = View.VISIBLE
                    tvOtherMessage.text = data.content
                    tvOtherTime.text = detailDateFormat(data.createdAt)
                }
            }
        }
    }

    fun setChat(chatDetail: List<ChatDetail>) {
        this.items = chatDetail
        notifyDataSetChanged()
    }
}