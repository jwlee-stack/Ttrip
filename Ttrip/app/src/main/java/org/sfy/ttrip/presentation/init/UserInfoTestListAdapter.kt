package org.sfy.ttrip.presentation.init

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.ListItemUserInfoTestBinding
import org.sfy.ttrip.domain.entity.user.SurveyItem

class UserInfoTestListAdapter(
    private val onUserTestClicked: (
        position: Int,
        record: Int
    ) -> Unit
) : RecyclerView.Adapter<UserInfoTestListAdapter.UserInfoTestListViewHolder>() {

    lateinit var binding: ListItemUserInfoTestBinding
    private var testList = listOf<SurveyItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoTestListViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_user_info_test,
            parent,
            false
        )
        return UserInfoTestListViewHolder(binding, onUserTestClicked)
    }

    override fun onBindViewHolder(holder: UserInfoTestListViewHolder, position: Int) {
        val viewHolder: UserInfoTestListViewHolder = holder
        viewHolder.onBind(testList[position])
    }

    override fun getItemCount(): Int = testList.size

    class UserInfoTestListViewHolder(
        val binding: ListItemUserInfoTestBinding,
        private val onUserTestClicked: (
            position: Int,
            record: Int
        ) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: SurveyItem) {
            binding.apply {
                tvUserInfoTestSubject.text = data.subject

                val checkData =
                    listOf(
                        binding.ivUserInfoTestCheck1,
                        binding.ivUserInfoTestCheck2,
                        binding.ivUserInfoTestCheck3,
                        binding.ivUserInfoTestCheck4,
                        binding.ivUserInfoTestCheck5,
                    )

                for (i in 0..4) {
                    if (data.score != 0 && data.score - 1 == i) {
                        checkData[i].setImageResource(R.drawable.ic_circle_check_test_check)
                    } else {
                        checkData[i].setImageResource(R.drawable.ic_circle_check_test)
                    }
                }

                for (i in 0..4) {
                    checkData[i].setOnClickListener {
                        checkData[i].setImageResource(R.drawable.ic_circle_check_test_check)
                        onUserTestClicked(data.index, i + 1)
                        for (j in 0..4) {
                            if (i != j) {
                                checkData[j].setImageResource(R.drawable.ic_circle_check_test)
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTestInfo(testInfo: List<SurveyItem>) {
        this.testList = testInfo
        notifyDataSetChanged()
    }
}