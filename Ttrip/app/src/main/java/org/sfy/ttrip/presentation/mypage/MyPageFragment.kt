package org.sfy.ttrip.presentation.mypage

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.common.util.makeMarkerImg
import org.sfy.ttrip.databinding.FragmentMypageBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import org.sfy.ttrip.presentation.init.InitActivity
import org.sfy.ttrip.presentation.init.SignUpInfoContentFragment.Companion.REQUEST_READ_STORAGE_PERMISSION
import org.sfy.ttrip.presentation.init.UserInfoViewModel
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage),
    LogoutDialogListener {

    private lateinit var callback: OnBackPressedCallback
    private var waitTime = 0L

    private lateinit var markerFile: File
    private val myPageViewModel by activityViewModels<MyPageViewModel>()
    private val userViewModel by activityViewModels<UserInfoViewModel>()
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - waitTime >= 2500) {
                    waitTime = System.currentTimeMillis()
                    showToast("뒤로가기 버튼을\n한번 더 누르면 종료됩니다.")
                } else {
                    requireActivity().finishAffinity()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onConfirmButtonClicked() {
        myPageViewModel.logout()
        userViewModel.postUserFcmToken(false)
        val intent = Intent(activity, InitActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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
            tvGetMyPosts.setOnClickListener {
                navigate(MyPageFragmentDirections.actionMyPageFragmentToMyPageBoardFragment())
            }
            tvGetMyBadges.setOnClickListener {
                navigate(MyPageFragmentDirections.actionMyPageFragmentToMyBadgesFragment())
                //navigate(MyPageFragmentDirections.actionMyPageFragmentToTutorialsFragment())
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
            if (myPageViewModel.isChanged.value!!) {
                myPageViewModel.updateBackgroundImg()
            }
        }
        myPageViewModel.profileImg.observe(viewLifecycleOwner) {
            if (myPageViewModel.isChanged.value!!) {
                saveImageToGallery(
                    makeMarkerImg(
                        requireContext(),
                        myPageViewModel.profileImg.value!!,
                        myPageViewModel.markerfile!!
                    )
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

    private fun setBackgroundView() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
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
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_STORAGE_PERMISSION
                )
            }
        }
    }

    private fun setProfileView() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
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
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
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

    private fun saveImageToGallery(bitmap: Bitmap) {
        // 권한 체크
        if (!checkPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ||
            !checkPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            requestPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return
        }

        // 그림 저장
        if (!imageExternalSave(
                requireActivity(),
                bitmap,
                requireActivity().getString(R.string.app_name)
            )
        ) {
            showToast("마커 이미지 생성에 실패 했습니다. 프로필 사진을 다시 선택해 주세요")
        }
        showToast("마커 이미지 생성에 성공했습니다")
        val file = File(markerFile.absolutePath)
        myPageViewModel.setMarkerImg(file)
    }

    private fun imageExternalSave(
        context: Context,
        bitmap: Bitmap,
        path: String
    ): Boolean {

        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val rootPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString()
            val dirName = "/" + path
            val fileName = System.currentTimeMillis().toString() + ".png"
            val savePath = File(rootPath + dirName)
            savePath.mkdirs()

            val file = File(savePath, fileName)
            if (file.exists()) file.delete()

            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
                markerFile = file
                // 갤러리 갱신
                context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())
                    )
                )

                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    private fun checkPermission(activity: Activity, permission: String): Boolean {
        val permissionChecker =
            ContextCompat.checkSelfPermission(activity.applicationContext, permission)
        // 권한이 없으면 권한 요청
        if (permissionChecker == PackageManager.PERMISSION_GRANTED) return true
        requestPermission(activity, permission)
        return false
    }

    private fun requestPermission(activity: Activity, permission: String) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), 1)
    }
}