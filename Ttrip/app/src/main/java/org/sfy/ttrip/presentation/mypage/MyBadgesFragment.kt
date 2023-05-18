package org.sfy.ttrip.presentation.mypage

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.common.util.GridSpaceItemDecoration
import org.sfy.ttrip.databinding.FragmentMyBadgesBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import org.sfy.ttrip.presentation.landmark.LandmarkViewModel

@AndroidEntryPoint
class MyBadgesFragment : BaseFragment<FragmentMyBadgesBinding>(R.layout.fragment_my_badges) {

    private val landmarkViewModel by activityViewModels<LandmarkViewModel>()
    private val myBadgesAdapter by lazy {
        MyBadgesAdapter(this::getBadgeInfo)
    }

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(true)
        setBadges()
        initListener()
    }

    private fun initListener() {
        binding.ivBack.setOnClickListener { popBackStack() }
    }

    private fun setBadges() {
        binding.rvMyBadges.apply {
            adapter = myBadgesAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
            addItemDecoration(GridSpaceItemDecoration(3, 11))
        }
        landmarkViewModel.badges.observe(viewLifecycleOwner) { response ->
            response?.let {
                myBadgesAdapter.setBadges(it)
                binding.tvContentMyBadgesNum.text = it.size.toString()
            }
        }
        landmarkViewModel.getBadges()
    }

    private fun getBadgeInfo(badgeName: String) {
        showToast("${badgeName}에서\n발급받은 뱃지입니다.")
    }
}