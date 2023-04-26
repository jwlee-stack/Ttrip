package org.sfy.ttrip.presentation.init

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentSignUpInformationBannerBinding
import org.sfy.ttrip.presentation.base.BaseFragment

class SignUpInformationBannerFragment :
    BaseFragment<FragmentSignUpInformationBannerBinding>(R.layout.fragment_sign_up_information_banner) {

    private var bannerPosition = -1

    override fun initView() {
        bannerPosition = arguments?.getInt("banner_position")!!
        with(bannerPosition) {

            val bannerData =
                listOf(
                    binding.clUserInfoNickName,
                    binding.clUserInfoDay,
                    binding.clUserInfoSex,
                    binding.clUserInfoProfile,
                    binding.clUserInfoTest,
                )

            when (bannerPosition) {
                0 -> {
                    changeVisibility(0, bannerData)
                }
                1 -> {
                    changeVisibility(1, bannerData)
                }
                2 -> {
                    changeVisibility(2, bannerData)
                }
                3 -> {
                    changeVisibility(3, bannerData)
                }
                4 -> {
                    changeVisibility(4, bannerData)
                }
            }
        }
    }

    private fun changeVisibility(position: Int, bannerData: List<ConstraintLayout>) {
        for (i: Int in 0..4) {
            bannerData[i].visibility = View.GONE
        }
        bannerData[position].visibility = View.VISIBLE
    }
}