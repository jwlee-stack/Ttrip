package org.sfy.ttrip.presentation.board

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentPostBoardContentBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

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
                binding.svBoardInfoDestination,
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
                                if (isTextInArray(binding.atNation, "nation")) {
                                    viewModel.postBoardNation(binding.atNation.text.toString())
                                    binding.tvNationValid.visibility = View.INVISIBLE
                                }
                            }

                            override fun onTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {
                                if (isTextInArray(binding.atNation, "nation")) {
                                    viewModel.postBoardNation(binding.atNation.text.toString())
                                    binding.tvNationValid.visibility = View.INVISIBLE
                                }
                            }

                            override fun afterTextChanged(p0: Editable?) {
                                if (isTextInArray(binding.atNation, "nation")) {
                                    viewModel.postBoardNation(binding.atNation.text.toString())
                                    binding.tvNationValid.visibility = View.INVISIBLE
                                } else {
                                    binding.tvNationValid.visibility = View.VISIBLE
                                }
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
                                if (isTextInArray(binding.atCity, "city")) {
                                    viewModel.postBoardCity(binding.atCity.text.toString())
                                    binding.tvCityValid.visibility = View.INVISIBLE
                                }
                            }

                            override fun onTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {
                                if (isTextInArray(binding.atCity, "city")) {
                                    viewModel.postBoardCity(binding.atCity.text.toString())
                                    binding.tvCityValid.visibility = View.INVISIBLE
                                }
                            }

                            override fun afterTextChanged(p0: Editable?) {
                                if (isTextInArray(binding.atCity, "city")) {
                                    viewModel.postBoardCity(binding.atCity.text.toString())
                                    binding.tvCityValid.visibility = View.INVISIBLE
                                } else {
                                    binding.tvCityValid.visibility = View.VISIBLE
                                }
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
                            val startDateCandidate = "$year-${
                                String.format(
                                    "%02d",
                                    monthOfYear + 1
                                )
                            }-${String.format("%02d", dayOfMonth)}"
                            viewModel.postStartDate(startDateCandidate)

                            binding.dpPostBoardEndDate.apply {
                                // DatePicker에서 선택한 날짜를 가져옴
                                val year = this.year
                                val month = this.month + 1 // month는 0부터 시작하므로 1을 더해줌
                                val day = this.dayOfMonth
                                // yyyy-MM-dd 형식으로 날짜를 저장
                                val endDateCandidate = "$year-${
                                    String.format(
                                        "%02d",
                                        month
                                    )
                                }-${String.format("%02d", day)}"

                                if (isEndAfterStart(
                                        viewModel.postStartDate.value ?: "",
                                        endDateCandidate
                                    )
                                ) {
                                    viewModel.postEndDate(endDateCandidate)
                                } else {
                                    viewModel.postEndDate("")
                                }
                            }
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

                        val endDateCandidate = "$year-${
                            String.format(
                                "%02d",
                                month
                            )
                        }-${String.format("%02d", day)}"

                        if (isEndAfterStart(
                                viewModel.postStartDate.value ?: "",
                                endDateCandidate
                            )
                        ) {
                            viewModel.postEndDate(endDateCandidate)
                        } else {
                            viewModel.postEndDate("")
                            showToast("종료 날짜가 시작 날짜보다 이전입니다. 다시 선택해주세요.")
                        }

                        setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                            val endDateCandidate = "$year-${
                                String.format(
                                    "%02d",
                                    monthOfYear + 1
                                )
                            }-${String.format("%02d", dayOfMonth)}"

                            if (isEndAfterStart(
                                    viewModel.postStartDate.value ?: "",
                                    endDateCandidate
                                )
                            ) {
                                viewModel.postEndDate(endDateCandidate)
                            } else {
                                viewModel.postEndDate("")
                                showToast("종료 날짜가 시작 날짜보다 이전입니다. 다시 선택해주세요.")
                            }
                        }
                    }
                    changeVisibility(4, contentData)
                }
            }
        }
    }

    private fun changeVisibility(position: Int, bannerData: List<View>) {
        for (i in 0..4) {
            bannerData[i].visibility = View.GONE
        }
        bannerData[position].visibility = View.VISIBLE
    }

    private fun isEndAfterStart(startDate: String, endDate: String): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val start: Date = format.parse(startDate) ?: return false
        val end: Date = format.parse(endDate) ?: return false
        return end >= start
    }

    fun isTextInArray(autoCompleteTextView: AutoCompleteTextView, kind: String): Boolean {
        val inputText = autoCompleteTextView.text.toString()
        return when (kind) {
            "nation" -> {
                val nationNamesArray = resources.getStringArray(R.array.nation_names)
                nationNamesArray.contains(inputText)
            }
            else -> {
                val cityNamesArray = resources.getStringArray(R.array.city_names)
                cityNamesArray.contains(inputText)
            }
        }
    }
}