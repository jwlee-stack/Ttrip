package org.sfy.ttrip.presentation.board

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentPostBoardContentBinding
import org.sfy.ttrip.presentation.base.BaseFragment

class PostBoardContentFragment :
    BaseFragment<FragmentPostBoardContentBinding>(R.layout.fragment_post_board_content) {

    private var bannerPosition = -1
    private val viewModel by activityViewModels<BoardViewModel>()

    override fun initView() {
        bannerPosition = arguments?.getInt("content_position")!!
        with(bannerPosition) {
            val contentData = listOf(
                binding.clBoardInfoTitle,
                binding.clBoardInfoContent,
                binding.clBoardInfoDestination,
                binding.clBoardInfoStartDay,
                binding.clBoardInfoEndDay
            )

            when (bannerPosition) {
                0 -> {
                    binding.etBoardInfoTitle.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                            viewModel.postBoardTitle(binding.etBoardInfoTitle.text.toString())
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            viewModel.postBoardTitle(binding.etBoardInfoTitle.text.toString())
                        }

                        override fun afterTextChanged(s: Editable?) {
                            viewModel.postBoardTitle(binding.etBoardInfoTitle.text.toString())
                        }
                    })
                    changeVisibility(0, contentData)
                }
                1 -> {
                    binding.etBoardInfoContent.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                            viewModel.postBoardContent(binding.etBoardInfoContent.text.toString())
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            viewModel.postBoardContent(binding.etBoardInfoContent.text.toString())
                        }

                        override fun afterTextChanged(s: Editable?) {
                            viewModel.postBoardContent(binding.etBoardInfoContent.text.toString())
                        }
                    })
                    changeVisibility(1, contentData)
                }
                2 -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.nation_names)
                    )
                    binding.atNation.apply {
                        setAdapter(adapter)
                        addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {
                                viewModel.postBoardNation(binding.atNation.text.toString())
                            }

                            override fun onTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {
                                viewModel.postBoardNation(binding.atNation.text.toString())
                            }

                            override fun afterTextChanged(p0: Editable?) {
                                viewModel.postBoardNation(binding.atNation.text.toString())
                            }
                        })
                    }

                    val adapter2 = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.city_names)
                    )
                    binding.atCity.apply {
                        setAdapter(adapter2)
                        addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {
                                viewModel.postBoardCity(binding.atNation.text.toString())
                            }

                            override fun onTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {
                                viewModel.postBoardCity(binding.atNation.text.toString())
                            }

                            override fun afterTextChanged(p0: Editable?) {
                                viewModel.postBoardCity(binding.atNation.text.toString())
                            }
                        })
                    }
                    changeVisibility(2, contentData)
                }
                3 -> {
                    binding.dpPostBoardStartDate.apply {
                        // DatePicker에서 선택한 날짜를 가져옴
                        val year = this.year
                        val month = this.month + 1 // month는 0부터 시작하므로 1을 더해줌
                        val day = this.dayOfMonth
                        // yyyy-MM-dd 형식으로 날짜를 저장
                        viewModel.postStartDate(
                            "$year-${
                                String.format(
                                    "%02d",
                                    month
                                )
                            }-${String.format("%02d", day)}"
                        )
                        setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                            viewModel.postStartDate(
                                "$year-${
                                    String.format(
                                        "%02d",
                                        month + 1
                                    )
                                }-${String.format("%02d", dayOfMonth)}"
                            )
                        }
                    }
                    changeVisibility(3, contentData)
                }
                4 -> {
                    binding.dpPostBoardEndDate.apply {
                        // DatePicker에서 선택한 날짜를 가져옴
                        val year = this.year
                        val month = this.month + 1 // month는 0부터 시작하므로 1을 더해줌
                        val day = this.dayOfMonth
                        // yyyy-MM-dd 형식으로 날짜를 저장
                        viewModel.postEndDate(
                            "$year-${
                                String.format(
                                    "%02d",
                                    month
                                )
                            }-${String.format("%02d", day)}"
                        )

                        setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                            viewModel.postEndDate(
                                "$year-${
                                    String.format(
                                        "%02d",
                                        month + 1
                                    )
                                }-${String.format("%02d", dayOfMonth)}"
                            )
                        }
                    }
                    changeVisibility(4, contentData)
                }
            }
        }
    }

    private fun changeVisibility(position: Int, bannerData: List<ConstraintLayout>) {
        for (i in 0..4) {
            bannerData[i].visibility = View.GONE
        }
        bannerData[position].visibility = View.VISIBLE
    }
}