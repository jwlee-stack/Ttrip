package org.sfy.ttrip.presentation.init

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentSignUpInfoBannerBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import java.io.File

class SignUpInfoBannerFragment :
    BaseFragment<FragmentSignUpInfoBannerBinding>(R.layout.fragment_sign_up_info_banner) {

    private var bannerPosition = -1
    private val userInfoTestListAdapter by lazy {
        UserInfoTestListAdapter()
    }

    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    private val fromAlbumActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        result.data?.let {
            if (it.data != null) {
                userInfoViewModel.setProfileImg(
                    it.data as Uri,
                    File(absolutelyPath(it.data, requireContext()))
                )
            }
        }
    }

    override fun initView() {
        initListener()

        bannerPosition = arguments?.getInt("banner_position")!!
        with(bannerPosition) {
            val bannerData =
                listOf(
                    binding.clUserInfoNickName,
                    binding.clUserInfoAge,
                    binding.clUserInfoSex,
                    binding.clUserInfoProfile,
                    binding.clUserInfoTest,
                )

            when (bannerPosition) {
                0 -> {
                    changeVisibility(0, bannerData)
                }
                1 -> {
                    binding.etUserInfoAge.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            userInfoViewModel.postAge(binding.etUserInfoAge.text.toString())
                        }

                        override fun afterTextChanged(s: Editable?) {
                            userInfoViewModel.postAge(binding.etUserInfoAge.text.toString())
                        }
                    })

                    changeVisibility(1, bannerData)
                }
                2 -> {
                    if (userInfoViewModel.userSex.value == "FEMALE") {
                        binding.apply {
                            tvUserInfoSexMale.setBackgroundResource(R.drawable.bg_rect_whisper_radius20)
                            tvUserInfoSexFemale.setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius20)
                        }
                    } else if (userInfoViewModel.userSex.value == "MALE") {
                        binding.apply {
                            tvUserInfoSexMale.setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius20)
                            tvUserInfoSexFemale.setBackgroundResource(R.drawable.bg_rect_whisper_radius20)
                        }
                    }
                    changeVisibility(2, bannerData)
                }
                3 -> {
                    binding.etUserInfoIntroduction.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            userInfoViewModel.postIntro(binding.etUserInfoIntroduction.text.toString())
                        }

                        override fun afterTextChanged(s: Editable?) {
                            userInfoViewModel.postIntro(binding.etUserInfoIntroduction.text.toString())
                        }
                    })

                    userInfoViewModel.profileImgUri.observe(this@SignUpInfoBannerFragment) {
                        when (it) {
                            null -> binding.ivUserInfoProfilePhotoBlack.visibility = View.VISIBLE
                            else -> binding.ivUserInfoProfilePhotoBlack.visibility = View.GONE
                        }
                    }

                    changeVisibility(3, bannerData)
                }
                4 -> {
                    changeVisibility(4, bannerData)
                }
            }
        }

        binding.rvUserInfoTest.apply {
            adapter = userInfoTestListAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun initListener() {
        binding.apply {
            tvUserInfoCheckNickName.setOnClickListener {
                userInfoViewModel.checkNickName()
            }

            tvUserInfoSexMale.setOnClickListener {
                tvUserInfoSexMale.setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius20)
                tvUserInfoSexFemale.setBackgroundResource(R.drawable.bg_rect_whisper_radius20)
                userInfoViewModel.postSex("MALE")
            }

            tvUserInfoSexFemale.setOnClickListener {
                tvUserInfoSexMale.setBackgroundResource(R.drawable.bg_rect_whisper_radius20)
                tvUserInfoSexFemale.setBackgroundResource(R.drawable.bg_rect_honey_suckle_radius20)
                userInfoViewModel.postSex("FEMALE")
            }

            clUserInfoProfile.setOnClickListener { setAlbumView() }
        }
    }

    private fun changeVisibility(position: Int, bannerData: List<ConstraintLayout>) {
        for (i in 0..4) {
            bannerData[i].visibility = View.GONE
        }
        bannerData[position].visibility = View.VISIBLE
    }

    private fun setAlbumView() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                fromAlbumActivityLauncher.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            }
            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_STORAGE_PERMISSION
                )
            }
        }
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

    companion object {
        const val REQUEST_READ_STORAGE_PERMISSION = 1
    }
}