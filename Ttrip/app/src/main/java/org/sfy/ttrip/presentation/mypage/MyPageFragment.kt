package org.sfy.ttrip.presentation.mypage

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
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
    private lateinit var markerFile: File

    private var waitTime = 0L

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
                val rotatedBitmap = rotateBitmap(
                    File(absolutelyPath(it.data as Uri, requireContext())).path,
                    BitmapFactory.decodeFile(
                        File(
                            absolutelyPath(
                                it.data as Uri,
                                requireContext()
                            )
                        ).path
                    )
                )
                val rotatedFile = createTempFile("rotated_", ".jpg", context?.cacheDir)
                val outputStream = FileOutputStream(rotatedFile)
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
                val requestFile = rotatedFile.asRequestBody("image/*".toMediaTypeOrNull())
                myPageViewModel.markerfile = File(
                    absolutelyPath(it.data as Uri, requireContext())
                )
                myPageViewModel.setProfileFile(
                    it.data as Uri, rotatedFile.name, requestFile
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(false)
        initListener()
        setUserProfile()
        initObserve()
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
        userViewModel.postUserFcmToken(false, "")
        val intent = Intent(activity, InitActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        showToast("로그아웃되었습니다.")
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
            }
            ivCertificateProfile.setOnClickListener {
                navigate(MyPageFragmentDirections.actionMyPageFragmentToCertificateProfileFragment())
            }
            ivProfileBackground.setOnClickListener {
                setBackgroundView()
            }
            ivProfileImage.setOnClickListener {
                setProfileView()
            }
            tvTutorials.setOnClickListener {
                navigate(MyPageFragmentDirections.actionMyPageFragmentToTutorialsFragment())
            }
        }
        binding.tvLogout.setOnClickListener {
            val dialog = LogoutDialog(requireContext(), this)
            dialog.show()
        }
    }

    private fun initObserve() {
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
        myPageViewModel.profileVerification.observe(viewLifecycleOwner) {
            if (it) binding.ivProfileVerification.visibility = View.VISIBLE
            else binding.ivProfileVerification.visibility = View.GONE
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setBackgroundView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES,
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
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        REQUEST_READ_STORAGE_PERMISSION
                    )
                }
            }
        } else {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE,
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
    }

    private fun setProfileView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES,
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
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        REQUEST_READ_STORAGE_PERMISSION
                    )
                }
            }
        } else {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE,
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!checkPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES) ||
                !checkPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)
            ) {
                requestPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)
                requestPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)
                return
            }
        } else {
            if (!checkPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ||
                !checkPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {
                requestPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                return
            }
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

    private fun rotateBitmap(filePath: String, bitmap: Bitmap): Bitmap {
        val orientation = getExifOrientation(filePath)
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

    private fun getExifOrientation(filePath: String): Int {
        val exif = ExifInterface(filePath)
        return exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
    }
}