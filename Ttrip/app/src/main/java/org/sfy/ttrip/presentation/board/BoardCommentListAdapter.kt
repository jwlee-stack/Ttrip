package org.sfy.ttrip.presentation.board

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.ListItemBoardDetailCommentBinding
import org.sfy.ttrip.domain.entity.board.BoardComment

class BoardCommentListAdapter(
    private val onBoardCommentItemClicked: (boardId: Int) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<BoardCommentListAdapter.BoardCommentListViewHolder>() {

    lateinit var binding: ListItemBoardDetailCommentBinding
    private var boardCommentList = listOf<BoardComment>()

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
            context
        )
    }

    override fun onBindViewHolder(holder: BoardCommentListViewHolder, position: Int) {
        val viewHolder: BoardCommentListViewHolder = holder
        viewHolder.onBind(boardCommentList[position])
    }

    override fun getItemCount(): Int = boardCommentList.size

    class BoardCommentListViewHolder(
        val binding: ListItemBoardDetailCommentBinding,
        private val onBoardCommentItemClicked: (boardId: Int) -> Unit,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: BoardComment) {
            binding.apply {
                root.setOnClickListener {

                }

                tvBoardDetailCommentUserNickName.text = data.applicantNickname
                tvBoardDetailCommentUserContent.text = data.requestContent
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBoardComment(boardCommentList: List<BoardComment>) {
        this.boardCommentList = boardCommentList
        notifyDataSetChanged()
    }
}