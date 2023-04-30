package org.sfy.ttrip.presentation.init

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.ListItemUserInfoTestBinding

class UserInfoTestListAdapter() :
    RecyclerView.Adapter<UserInfoTestListAdapter.UserInfoTestListViewHolder>() {

    lateinit var binding: ListItemUserInfoTestBinding
    private var testList = listOf<String>(
        "번화한 도시보다 자연 풍경이 좋다",
        "미리 여행 계획을 세워야 마음이 편하다",
        "경유보다 직항을 선호한다",
        "숙소에는 경비를 줄이고 싶다",
        "맛집은 무조건 찾아가야 한다",
        "택시보다 대중교통을 이용한다",
        "타이트한 여행을 추구한다",
        "명소 관광보다 쇼핑이 좋다"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoTestListViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_user_info_test,
            parent,
            false
        )
        return UserInfoTestListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserInfoTestListViewHolder, position: Int) {
        val viewHolder: UserInfoTestListViewHolder = holder
        viewHolder.onBind(testList[position])
    }

    override fun getItemCount(): Int = testList.size

    class UserInfoTestListViewHolder(
        val binding: ListItemUserInfoTestBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: String) {
            binding.tvUserInfoTestSubject.text = data
        }
    }
}