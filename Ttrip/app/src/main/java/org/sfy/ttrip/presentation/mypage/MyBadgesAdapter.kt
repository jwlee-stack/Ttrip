package org.sfy.ttrip.presentation.mypage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.ItemBadgeBinding
import org.sfy.ttrip.domain.entity.landmark.BadgeItem

class MyBadgesAdapter(
    private val onBadgeItemClicked: (badgeName: String) -> Unit
) : RecyclerView.Adapter<MyBadgesAdapter.BadgeViewHolder>() {

    lateinit var binding: ItemBadgeBinding
    private var badgeItems = listOf<BadgeItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_badge,
            parent,
            false
        )
        return BadgeViewHolder(binding, onBadgeItemClicked)
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        val viewHolder: BadgeViewHolder = holder
        viewHolder.onBind(badgeItems[position])
    }

    override fun getItemCount(): Int = badgeItems.size

    class BadgeViewHolder(
        val binding: ItemBadgeBinding,
        private val onBadgeItemClicked: (badgeName: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: BadgeItem) {
            binding.badge = data
            binding.apply {
                root.setOnClickListener {
                    onBadgeItemClicked(data.badgeName)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBadges(badgeItem: List<BadgeItem>) {
        this.badgeItems = badgeItem
        notifyDataSetChanged()
    }
}