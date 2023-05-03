package org.sfy.ttrip.presentation.board

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.ListItemBoardContentBinding
import org.sfy.ttrip.domain.entity.board.BoardBrief
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class BoardListAdapter(
    private val onBoardItemClicked: (boardId: Int) -> Unit,
    private val context: Context
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
        return BoardListViewHolder(binding, onBoardItemClicked, context)
    }

    override fun onBindViewHolder(holder: BoardListViewHolder, position: Int) {
        val viewHolder: BoardListViewHolder = holder
        viewHolder.onBind(boardList[position])
    }

    override fun getItemCount(): Int = boardList.size

    class BoardListViewHolder(
        val binding: ListItemBoardContentBinding,
        private val onBoardItemClicked: (boardId: Int) -> Unit,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: BoardBrief) {
            binding.apply {
                root.setOnClickListener {
                    onBoardItemClicked(data.articleId)
                }

                if (data.dueDay <= 3) {
                    clTicketTop.setBackgroundResource(R.drawable.bg_rect_old_rose_top_radius20)
                    tvDate.setTextColor(ContextCompat.getColor(context, R.color.old_rose))
                    tvNationCity.setTextColor(ContextCompat.getColor(context, R.color.old_rose))
                    tvBoardDDay.setTextColor(ContextCompat.getColor(context, R.color.old_rose))
                    ivTicketDDayAirplane.setBackgroundResource(R.drawable.ic_airplane_red)
                } else if (data.dueDay <= 10) {
                    clTicketTop.setBackgroundResource(R.drawable.bg_rect_ming_top_radius20)
                    tvDate.setTextColor(ContextCompat.getColor(context, R.color.ming))
                    tvNationCity.setTextColor(ContextCompat.getColor(context, R.color.ming))
                    tvBoardDDay.setTextColor(ContextCompat.getColor(context, R.color.ming))
                    ivTicketDDayAirplane.setBackgroundResource(R.drawable.ic_airplane_green)
                } else {
                    clTicketTop.setBackgroundResource(R.drawable.bg_rect_royal_blue_top_radius20)
                    tvDate.setTextColor(ContextCompat.getColor(context, R.color.royal_blue))
                    tvNationCity.setTextColor(ContextCompat.getColor(context, R.color.royal_blue))
                    tvBoardDDay.setTextColor(ContextCompat.getColor(context, R.color.royal_blue))
                    ivTicketDDayAirplane.setBackgroundResource(R.drawable.ic_airplane_blue)
                }

                // "2023-05-02T09:28:21"와 같은 문자열로부터 LocalDateTime 객체 생성
                val dateString = data.createdAt
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
                val dateTime = LocalDateTime.parse(dateString, formatter)

                // 현재 날짜와 비교하여 포맷팅
                val now = LocalDateTime.now()
                if (dateTime.toLocalDate() == now.toLocalDate()) {
                    // 현재 날짜와 동일한 경우 시간만 표시
                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
                    val formattedTime = dateTime.format(timeFormatter)
                    tvBoardTime.text = formattedTime // 예시: 09:28
                } else {
                    // 현재 날짜와 다른 경우 날짜와 시간을 표시
                    val dateFormatter = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)
                    val formattedDate = dateTime.format(dateFormatter)
                    tvBoardTime.text = formattedDate // 예시: 2 May
                }

                tvBoardTitle.text = data.title
                tvBoardDDay.text = "D-${data.dueDay}"
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