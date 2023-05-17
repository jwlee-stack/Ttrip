package org.sfy.ttrip.presentation.board

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.common.util.BindingAdapters.setProfileImg
import org.sfy.ttrip.databinding.ListItemRecommendBoardBinding
import org.sfy.ttrip.domain.entity.board.RecommendBoard

class RecommendBoardAdapter(
    private val onBoardItemClicked: (boardId: Int, dDay: Int) -> Unit
) : RecyclerView.Adapter<RecommendBoardAdapter.CardViewHolder>() {

    private var boardList: List<RecommendBoard> = listOf()
    lateinit var binding: ListItemRecommendBoardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_recommend_board,
            parent,
            false
        )
        return CardViewHolder(binding, onBoardItemClicked)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val viewHolder: CardViewHolder = holder
        viewHolder.onBind(boardList[position])
    }

    override fun getItemCount(): Int = boardList.size

    class CardViewHolder(
        private val binding: ListItemRecommendBoardBinding,
        private val onBoardItemClicked: (boardId: Int, dDay: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: RecommendBoard) {
            binding.apply {
                root.setOnClickListener {
                    onBoardItemClicked(data.articleId, data.dueDay.toInt())
                }
                recommendBoard = data
                ivRecommendBoardDetailUserProfile.setProfileImg(data.imgPath)
                tvDetailDuring.text = "${data.startDate} ~ ${data.endDate}"
                tvDetailNationCity.text = "${data.nation} - ${data.city}"

                val inputDateTime = data.createdAt
                val date = inputDateTime.substringBefore("T")
                val time = inputDateTime.substringAfter("T").substringBeforeLast(":")
                tvRecommendBoardDetailUserDate.text = "${date.replace("-", ".")} $time"

                tvUserSimilarity.apply {
                    if (data.similarity <= 50) {
                        setTextColor(ContextCompat.getColor(context, R.color.lochmara2))
                        setBackgroundResource(R.drawable.bg_rect_lochmara2_alice_blue2_radius10_stroke1)
                    } else if (data.similarity <= 80) {
                        setTextColor(ContextCompat.getColor(context, R.color.limerick))
                        setBackgroundResource(R.drawable.bg_rect_limerick_twilight_blue_radius10_stroke1)
                    } else {
                        setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.medium_orchid
                            )
                        )
                        setBackgroundResource(R.drawable.bg_rect_wisteria_white_lilac_radius10_stroke1)
                    }
                    text = "${data.similarity}%"
                }

                binding.apply {
                    if (data.dueDay.toInt() == -2) {
                        clRecommendBoardDetailTitle.setBackgroundResource(R.drawable.bg_rect_dim_gray_top_radius20)
                    } else if (data.dueDay.toInt() == -1) {
                        clRecommendBoardDetailTitle.setBackgroundResource(R.drawable.bg_rect_neon_blue_top_radius20)
                    } else if (data.dueDay.toInt() <= 3) {
                        clRecommendBoardDetailTitle.setBackgroundResource(R.drawable.bg_rect_old_rose_top_radius20)
                    } else if (data.dueDay.toInt() <= 10) {
                        clRecommendBoardDetailTitle.setBackgroundResource(R.drawable.bg_rect_ming_top_radius20)
                    } else {
                        clRecommendBoardDetailTitle.setBackgroundResource(R.drawable.bg_rect_royal_blue_top_radius20)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRecommendBoard(recommendBoardList: List<RecommendBoard>) {
        this.boardList = recommendBoardList
        notifyDataSetChanged()
    }
}