package org.sfy.ttrip.presentation.live

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.ItemLiveInfoBinding
import org.sfy.ttrip.domain.entity.live.LiveUser

class LiveUserAdapter(
    private val onItemClicked: (songId: String) -> Unit
) : RecyclerView.Adapter<LiveUserAdapter.ViewHolder>() {

    private var items: List<LiveUser> = listOf()
    lateinit var binding: ItemLiveInfoBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_live_info, parent, false
        )
        return ViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(
        private val binding: ItemLiveInfoBinding,
        private val onItemClicked: (songId: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: LiveUser) {
            binding.apply {
                user = data
                root.setOnClickListener {
                    onItemClicked(data.memberUuid)
                }
            }
        }
    }

    fun setLiveUser(songItem: List<LiveUser>) {
        this.items = songItem
        notifyDataSetChanged()
    }
}