package org.sfy.ttrip.presentation.mypage

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentPreferenceTestAgainBinding
import org.sfy.ttrip.domain.entity.user.SurveyItem
import org.sfy.ttrip.presentation.base.BaseFragment
import org.sfy.ttrip.presentation.init.UserInfoTestListAdapter

@AndroidEntryPoint
class PreferenceTestAgainFragment :
    BaseFragment<FragmentPreferenceTestAgainBinding>(R.layout.fragment_preference_test_again) {

    private val myPageViewModel by activityViewModels<MyPageViewModel>()
    private val userInfoTestListAdapter by lazy {
        UserInfoTestListAdapter(this::onUserTestClicked)
    }
    private var testList = listOf(
        SurveyItem(
            "번화한 도시보다 자연 풍경이 좋다", 0, 0
        ),
        SurveyItem(
            "미리 여행 계획을 세워야 마음이 편하다", 1, 0
        ),
        SurveyItem(
            "경유보다 직항을 선호한다", 2, 0
        ),
        SurveyItem(
            "숙소에는 경비를 줄이고 싶다", 3, 0
        ),
        SurveyItem(
            "맛집은 무조건 찾아가야 한다", 4, 0
        ),
        SurveyItem(
            "택시보다 대중교통을 이용한다", 5, 0
        ),
        SurveyItem(
            "개인별 경비를 선호한다", 6, 0
        ),
        SurveyItem(
            "타이트한 여행을 추구한다", 7, 0
        ),
        SurveyItem(
            "명소 관광보다 쇼핑이 좋다", 8, 0
        )
    )

    override fun initView() {
        initRecyclerView()
        observeSurvey()
    }

    private fun initRecyclerView() {
        binding.rvUserInfoTest.apply {
            adapter = userInfoTestListAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            userInfoTestListAdapter.setTestInfo(testList)
        }
    }

    private fun onUserTestClicked(position: Int, record: Int) {
        testList[position].score = record
        userInfoTestListAdapter.setTestInfo(testList)
        myPageViewModel.checkSurvey(position, record)
    }

    private fun observeSurvey() {
        myPageViewModel.userTest.observe(viewLifecycleOwner) {
            if (it.preferCheapHotelThanComfort != 0 &&
                it.preferCheapTraffic != 0 &&
                it.preferGoodFood != 0 &&
                it.preferDirectFlight != 0 &&
                it.preferPlan != 0 &&
                it.preferTightSchedule != 0 &&
                it.preferShoppingThanTour != 0 &&
                it.preferNatureThanCity != 0 &&
                it.preferPersonalBudget != 0
            ) {
                binding.tvConfirm.apply {
                    isEnabled = true
                    setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius10)
                    setOnClickListener {
                        myPageViewModel.apply {
                            updatePreferences(
                                userTest.value!!.preferNatureThanCity,
                                userTest.value!!.preferCheapTraffic,
                                userTest.value!!.preferDirectFlight,
                                userTest.value!!.preferGoodFood,
                                userTest.value!!.preferNatureThanCity,
                                userTest.value!!.preferPersonalBudget,
                                userTest.value!!.preferPlan,
                                userTest.value!!.preferShoppingThanTour,
                                userTest.value!!.preferTightSchedule
                            )
                        }
                        popBackStack()
                        showToast("취향테스트가 완료되었습니다.")
                    }
                }
            } else {
                binding.tvConfirm.apply {
                    isEnabled = false
                    setBackgroundResource(R.drawable.bg_rect_gainsboro_radius10)
                }
            }
        }
    }
}