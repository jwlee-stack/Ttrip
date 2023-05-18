package org.sfy.ttrip.presentation.mypage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.sfy.ttrip.MainActivity
import org.sfy.ttrip.R
import org.sfy.ttrip.common.util.BindingAdapters.setProfileImg
import org.sfy.ttrip.databinding.FragmentCertificateProfileBinding
import org.sfy.ttrip.presentation.base.BaseFragment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat

@AndroidEntryPoint
class CertificateProfileFragment :
    BaseFragment<FragmentCertificateProfileBinding>(R.layout.fragment_certificate_profile) {

    private val targetWidth = 1280
    private val targetHeight = 960
    private val myPageViewModel by activityViewModels<MyPageViewModel>()
    private lateinit var realUri: Uri

    override fun initView() {
        (activity as MainActivity).hideBottomNavigation(true)
        binding.vm = myPageViewModel
        binding.ivRecentProfileImage.setProfileImg(myPageViewModel.userProfile.value?.profileImgPath)
        initListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        myPageViewModel.clearCertificateProfile()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    realUri.let { uri ->
                        when (myPageViewModel.certificateNum) {
                            1 -> {
                                binding.ivInputImageCamera1.visibility = View.GONE
                                val rotatedBitmap = rotateBitmap(
                                    File(absolutelyPath(uri, requireContext())).path,
                                    BitmapFactory.decodeFile(
                                        File(
                                            absolutelyPath(
                                                uri,
                                                requireContext()
                                            )
                                        ).path
                                    ),
                                    800
                                )
                                val rotatedFile =
                                    createTempFile("rotated_", ".jpg", context?.cacheDir)
                                val outputStream = FileOutputStream(rotatedFile)
                                rotatedBitmap.compress(
                                    Bitmap.CompressFormat.JPEG,
                                    100,
                                    outputStream
                                )
                                outputStream.close()
                                val requestFile =
                                    rotatedFile.asRequestBody("image/*".toMediaTypeOrNull())
                                myPageViewModel.setCertificateImg1(
                                    uri, rotatedFile.name, requestFile
                                )
                            }
                            2 -> {
                                binding.ivInputImageCamera2.visibility = View.GONE
                                val rotatedBitmap = rotateBitmap(
                                    File(absolutelyPath(uri, requireContext())).path,
                                    BitmapFactory.decodeFile(
                                        File(
                                            absolutelyPath(
                                                uri,
                                                requireContext()
                                            )
                                        ).path
                                    ),
                                    800
                                )
                                val rotatedFile =
                                    createTempFile("rotated_", ".jpg", context?.cacheDir)
                                val outputStream = FileOutputStream(rotatedFile)
                                rotatedBitmap.compress(
                                    Bitmap.CompressFormat.JPEG,
                                    100,
                                    outputStream
                                )
                                outputStream.close()
                                val requestFile =
                                    rotatedFile.asRequestBody("image/*".toMediaTypeOrNull())
                                myPageViewModel.setCertificateImg2(
                                    uri, rotatedFile.name, requestFile
                                )
                            }
                            3 -> {
                                binding.ivInputImageCamera3.visibility = View.GONE
                                val rotatedBitmap = rotateBitmap(
                                    File(absolutelyPath(uri, requireContext())).path,
                                    BitmapFactory.decodeFile(
                                        File(
                                            absolutelyPath(
                                                uri,
                                                requireContext()
                                            )
                                        ).path
                                    ),
                                    800
                                )
                                val rotatedFile =
                                    createTempFile("rotated_", ".jpg", context?.cacheDir)
                                val outputStream = FileOutputStream(rotatedFile)
                                rotatedBitmap.compress(
                                    Bitmap.CompressFormat.JPEG,
                                    100,
                                    outputStream
                                )
                                outputStream.close()
                                val requestFile =
                                    rotatedFile.asRequestBody("image/*".toMediaTypeOrNull())
                                myPageViewModel.setCertificateImg3(
                                    uri, rotatedFile.name, requestFile
                                )
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
    }

    private fun initListener() {
        binding.apply {
            ivInputImage1.setOnClickListener {
                myPageViewModel.certificateNum = 1
                requirePermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_CAMERA)
            }
            ivInputImage2.setOnClickListener {
                myPageViewModel.certificateNum = 2
                requirePermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_CAMERA)
            }
            ivInputImage3.setOnClickListener {
                myPageViewModel.certificateNum = 3
                requirePermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_CAMERA)
            }
            tvSubmit.setOnClickListener {
                if (myPageViewModel.certificateImg1.value == null ||
                    myPageViewModel.certificateImg2.value == null ||
                    myPageViewModel.certificateImg3.value == null
                ) {
                    showToast("사진 3개를 모두 등록해주세요!")
                } else {
                    myPageViewModel.certificateProfile()
                    showToast("인증 신청되었습니다. 약 1분이 소요됩니다.")
                    popBackStack()
                }
            }
        }
    }

    private fun requirePermissions(permissions: Array<String>, requestCode: Int) {
        val isAllPermissionsGranted = permissions.all {
            checkSelfPermission(
                requireContext(), it
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllPermissionsGranted) {
            permissionGranted(requestCode)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), permissions, requestCode)
        }
    }

    private fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            PERMISSION_CAMERA -> openCamera()
        }
    }

    private fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERMISSION_CAMERA -> showToast("권한을 허용해주세요.")
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

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createImageUri(newFileName(), "image/jpg")?.let { uri ->
            realUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            startActivityForResult(intent, REQUEST_CAMERA)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun newFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "$filename.jpg"
    }

    private fun createImageUri(filename: String, mimeType: String): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
    }

    private fun rotateBitmap(filePath: String, bitmap: Bitmap, targetSize: Int): Bitmap {
        val orientation = getExifOrientation(filePath)
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return resizeBitmapMaintainingAspectRatio(
                bitmap,
                targetSize
            )
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
            else -> return resizeBitmapMaintainingAspectRatio(bitmap, targetSize)
        }
        val rotatedBitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
        return resizeBitmapMaintainingAspectRatio(rotatedBitmap, targetSize)
    }

    private fun resizeBitmapMaintainingAspectRatio(bitmap: Bitmap, targetSize: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height

        val scaledWidth: Int
        val scaledHeight: Int
        val scaleFactor: Float

        if (originalWidth > originalHeight) {
            scaleFactor = targetSize.toFloat() / originalWidth
            scaledWidth = targetSize
            scaledHeight = (originalHeight * scaleFactor).toInt()
        } else {
            scaleFactor = targetSize.toFloat() / originalHeight
            scaledHeight = targetSize
            scaledWidth = (originalWidth * scaleFactor).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
    }

    private fun getExifOrientation(filePath: String): Int {
        val exif = ExifInterface(filePath)
        return exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
    }

    companion object {
        const val PERMISSION_CAMERA = 100
        const val REQUEST_CAMERA = 101
    }
}
