package org.sfy.ttrip.presentation.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.ItemChatBinding
import org.sfy.ttrip.domain.entity.chat.ChatDetail
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ChatDetailAdapter : RecyclerView.Adapter<ChatDetailAdapter.ChatDetailViewHolder>() {

    private var items: List<ChatDetail> = listOf()
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

            val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

            binding.apply {
                if (data.isMine) {
                    binding.apply {
                        clOtherMessage.visibility = View.GONE
                        tvMyMessage.text = data.content
                        tvMyTime.text = data.createdDate.format(formatter)
                    }
                } else {
                    binding.apply {
                        clMyMessage.visibility = View.GONE
                        tvOtherMessage.text = data.content
                        tvOtherTime.text = data.createdDate.format(formatter)
                    }
                }
            }
        }
    }

    fun setChat(chatDetail: List<ChatDetail>) {
        this.items = chatDetail
        notifyDataSetChanged()
    }
}