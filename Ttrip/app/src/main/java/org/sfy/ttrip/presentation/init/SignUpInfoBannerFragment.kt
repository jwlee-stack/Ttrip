package org.sfy.ttrip.presentation.init

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentSignUpInfoBannerBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import java.io.File

class SignUpInfoBannerFragment :
    BaseFragment<FragmentSignUpInfoBannerBinding>(R.layout.fragment_sign_up_info_banner) {

    private var bannerPosition = -1
    private val joinViewModel by viewModels<UserInfoViewModel>()
    private val fromAlbumActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        result.data?.let {
            if (it.data != null) {
                joinViewModel.setProfileImg(
                    it.data as Uri,
                    File(absolutelyPath(it.data, requireContext()))
                )
            }
        }
    }

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
        for (i in 0..4) {
            bannerData[i].visibility = View.GONE
        }
        bannerData[position].visibility = View.VISIBLE
    }

    private fun absolutelyPath(path: Uri?, context: Context): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        val result = c?.getString(index!!)
        c?.close()
        return result!!
    }
}