package org.sfy.ttrip.presentation.board

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.ListItemBoardContentBinding
import org.sfy.ttrip.domain.entity.board.BoardBrief

class BoardListAdapter(
    private val onBoardItemClicked: (boardId: Int) -> Unit
) : RecyclerView.Adapter<BoardListAdapter.BoardListViewHolder>() {

    lateinit var binding: ListItemBoardContentBinding
    private var boardList = listOf<BoardBrief>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardListViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_board_content,
            parent,
            false
        )
        return BoardListViewHolder(binding, onBoardItemClicked)
    }

    override fun onBindViewHolder(holder: BoardListViewHolder, position: Int) {
        val viewHolder: BoardListViewHolder = holder
        viewHolder.onBind(boardList[position])
    }

    override fun getItemCount(): Int = boardList.size

    class BoardListViewHolder(
        val binding: ListItemBoardContentBinding,
        private val onBoardItemClicked: (boardId: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: BoardBrief) {
            binding.apply {

                tvBoardTitle.text = data.title
                tvNationCity.text = "${data.nation} - ${data.city}"
                tvDate.text = "${data.startDate} ~ ${data.endDate}"
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBoard(boardBriefList: List<BoardBrief>) {
        this.boardList = boardBriefList
        notifyDataSetChanged()
    }
}