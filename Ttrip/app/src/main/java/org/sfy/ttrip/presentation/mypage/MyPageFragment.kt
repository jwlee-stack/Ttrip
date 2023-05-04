package org.sfy.ttrip.presentation.mypage

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.databinding.FragmentMypageBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import org.sfy.ttrip.presentation.init.InitActivity
import org.sfy.ttrip.presentation.init.SignUpInfoContentFragment.Companion.REQUEST_READ_STORAGE_PERMISSION
import java.io.File

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage),
    LogoutDialogListener {

    private val myPageViewModel by activityViewModels<MyPageViewModel>()
    private val fromActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        result.data?.let {
            if (it.data != null) {
                myPageViewModel.setBackgroundFile(
                    it.data as Uri,
                    File(absolutelyPath(it.data, requireContext()))
                )
            }
        }
    }
    private val fromProfileActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        result.data?.let {
            if (it.data != null) {
                myPageViewModel.setProfileFile(
                    it.data as Uri,
                    File(absolutelyPath(it.data, requireContext()))
                )
            }
        }
    }

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(false)
        initListener()
        setUserProfile()
        observeImg()
    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume", "onResume: ")
        CoroutineScope(Dispatchers.Main).launch {
            delay(300)
            myPageViewModel.getUserProfile()
        }
    }

    override fun onConfirmButtonClicked() {
        myPageViewModel.logout()
        val intent = Intent(activity, InitActivity::class.java)
        startActivity(intent)
        showToast("로그아웃되었습니다.")
    }

    private fun initListener() {
        binding.apply {
            ivEditProfile.setOnClickListener {
                navigate(MyPageFragmentDirections.actionMyPageFragmentToEditProfileFragment())
            }
            tvTestAgain.setOnClickListener {
                navigate(MyPageFragmentDirections.actionMyPageFragmentToPreferenceTestAgainFragment())
            }
            ivProfileBackground.setOnClickListener {
                setBackgroundView()
            }
            ivProfileImage.setOnClickListener {
                setProfileView()
            }
        }
        binding.tvLogout.setOnClickListener {
            val dialog = LogoutDialog(requireContext(), this)
            dialog.show()
        }
    }

    private fun observeImg() {
        myPageViewModel.backgroundImg.observe(viewLifecycleOwner) {
            myPageViewModel.updateBackgroundImg()
        }
        myPageViewModel.profileImg.observe(viewLifecycleOwner) {
            myPageViewModel.updateProfileImg()
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

    private fun setBackgroundView() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                fromActivityLauncher.launch(
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

    private fun setProfileView() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                fromProfileActivityLauncher.launch(
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

    private fun setUserProfile() {
        myPageViewModel.userProfile.observe(viewLifecycleOwner) { response ->
            response?.let {
                binding.userProfile = it
                myPageViewModel.postNickname(it.nickname)
                myPageViewModel.postAge(it.age.toString())
            }
        }
        myPageViewModel.getUserProfile()
    }
}