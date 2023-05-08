package org.sfy.ttrip.presentation.board

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.common.util.BindingAdapters.setProfileImg
import org.sfy.ttrip.databinding.ListItemBoardDetailCommentBinding
import org.sfy.ttrip.domain.entity.board.BoardComment

class BoardCommentListAdapter(
    private val onBoardCommentItemClicked: (nickName: String, boardId: Int) -> Unit,
    private val context: Context,
    private val boardId: Int
) : RecyclerView.Adapter<BoardCommentListAdapter.BoardCommentListViewHolder>() {

    lateinit var binding: ListItemBoardDetailCommentBinding
    private var boardCommentList = listOf<BoardComment>()
    private var isMine = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardCommentListViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_board_detail_comment,
            parent,
            false
        )
        return BoardCommentListViewHolder(
            binding,
            onBoardCommentItemClicked,
            context,
            isMine,
            boardId
        )
    }

    override fun onBindViewHolder(holder: BoardCommentListViewHolder, position: Int) {
        val viewHolder: BoardCommentListViewHolder = holder
        viewHolder.onBind(boardCommentList[position])
    }

    override fun getItemCount(): Int = boardCommentList.size

    class BoardCommentListViewHolder(
        val binding: ListItemBoardDetailCommentBinding,
        private val onBoardCommentItemClicked: (nickName: String, boardId: Int) -> Unit,
        private val context: Context,
        private val isMine: Boolean,
        private val boardId: Int
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: BoardComment) {
            binding.apply {
                tvBoardDetailCommentUserNickName.text = data.applicantNickname
                tvBoardDetailCommentUserContent.text = data.requestContent
                ivBoardDetailCommentUserProfile.setProfileImg(data.imgPath)

                if (isMine) {
                    root.setOnClickListener {
                        onBoardCommentItemClicked(data.applicantNickname, boardId)
                    }
                    tvBoardDetailCommentUserPercent.visibility = View.VISIBLE
                    tvBoardDetailCommentUserPercent.text = "${data.similarity.toInt()}%"

                    if (data.similarity <= 50) {
                        clBoardDetailCommentItem.setBackgroundResource(R.drawable.bg_rect_lochmara_white_radius10_stroke1)
                        tvBoardDetailCommentUserPercent.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.lochmara
                            )
                        )
                        tvBoardDetailCommentUserPercent.setBackgroundResource(R.drawable.bg_rect_lochmara2_alice_blue2_radius10_stroke1)
                    } else if (data.similarity <= 80) {
                        clBoardDetailCommentItem.setBackgroundResource(R.drawable.bg_rect_limerick_white_radius10_stroke1)
                        tvBoardDetailCommentUserPercent.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.limerick
                            )
                        )
                        tvBoardDetailCommentUserPercent.setBackgroundResource(R.drawable.bg_rect_limerick_twilight_blue_radius10_stroke1)
                    } else {
                        clBoardDetailCommentItem.setBackgroundResource(R.drawable.bg_rect_medium_white_radius10_stroke1)
                        tvBoardDetailCommentUserPercent.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.medium_orchid
                            )
                        )
                        tvBoardDetailCommentUserPercent.setBackgroundResource(R.drawable.bg_rect_medium_orchid_white_lilac_radius10_stroke1)
                    }
                } else {
                    clBoardDetailCommentItem.setBackgroundResource(R.drawable.bg_rect_gainsboro_white_radius10_stroke1)
                    tvBoardDetailCommentUserPercent.visibility = View.INVISIBLE
                    tvBoardDetailCommentUserPercent.text = data.similarity.toInt().toString()
                    root.isEnabled = false
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBoardComment(boardCommentList: List<BoardComment>, isMine: Boolean) {
        this.boardCommentList = boardCommentList
        this.isMine = isMine
        notifyDataSetChanged()
    }
}