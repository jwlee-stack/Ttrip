package org.sfy.ttrip.presentation.mypage

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.sfy.ttrip.ApplicationClass
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.user.CheckDuplicationResponse
import org.sfy.ttrip.domain.entity.board.BoardBrief
import org.sfy.ttrip.domain.entity.mypage.BackgroundImg
import org.sfy.ttrip.domain.entity.mypage.UserProfile
import org.sfy.ttrip.domain.entity.user.UserTest
import org.sfy.ttrip.domain.usecase.mypage.*
import org.sfy.ttrip.domain.usecase.user.CheckDuplicationUseCase
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val updatePreferencesUseCase: UpdatePreferencesUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val checkDuplicationUseCase: CheckDuplicationUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val updateBackgroundImgUseCase: UpdateBackgroundImgUseCase,
    private val updateProfileImgUseCase: UpdateProfileImgUseCase,
    private val getMyPostsUseCase: GetMyPostsUseCase,
    private val certificateProfileUseCase: CertificateProfileUseCase
) : ViewModel() {

    private val _posts = MutableLiveData<List<BoardBrief>?>()
    val posts: LiveData<List<BoardBrief>?> = _posts

    private val _userTest: MutableLiveData<UserTest> =
        MutableLiveData(UserTest(0, 0, 0, 0, 0, 0, 0, 0, 0))
    val userTest: LiveData<UserTest> = _userTest

    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> = _userProfile

    private val _nickname: MutableLiveData<String?> = MutableLiveData()
    val nickname: LiveData<String?> = _nickname

    private val _age: MutableLiveData<String?> = MutableLiveData("")
    val age: LiveData<String?> = _age

    private val _gender: MutableLiveData<String?> = MutableLiveData("")
    val gender: LiveData<String?> = _gender

    private val _intro: MutableLiveData<String?> = MutableLiveData()
    val intro: LiveData<String?> = _intro

    private val _isDuplicate: MutableLiveData<Boolean?> = MutableLiveData(false)
    val isDuplicate: MutableLiveData<Boolean?> = _isDuplicate

    private val _backgroundImg: MutableLiveData<Uri?> = MutableLiveData()
    val backgroundImg: MutableLiveData<Uri?> = _backgroundImg

    private var backgroundFileMultiPart: MultipartBody.Part? = null

    private val _profileImg: MutableLiveData<Uri?> = MutableLiveData()
    val profileImg: MutableLiveData<Uri?> = _profileImg

    private var profileFileMultiPart: MultipartBody.Part? = null

    private val _certificateImg1: MutableLiveData<Uri?> = MutableLiveData()
    val certificateImg1: MutableLiveData<Uri?> = _certificateImg1

    private var certificateFile1MultiPart: MultipartBody.Part? = null

    private val _certificateImg2: MutableLiveData<Uri?> = MutableLiveData()
    val certificateImg2: MutableLiveData<Uri?> = _certificateImg2

    private var certificateFile2MultiPart: MultipartBody.Part? = null

    private val _certificateImg3: MutableLiveData<Uri?> = MutableLiveData()
    val certificateImg3: MutableLiveData<Uri?> = _certificateImg3

    private var certificateFile3MultiPart: MultipartBody.Part? = null

    var certificateNum = 0

    var markerfile: File? = null
    private var markerFileMultiPart: MultipartBody.Part? = null

    private val _isChanged: MutableLiveData<Boolean> = MutableLiveData(false)
    val isChanged: MutableLiveData<Boolean> = _isChanged

    suspend fun checkDuplication() =
        viewModelScope.async {
            when (val value = checkDuplicationUseCase(nickname.value!!)) {
                is Resource.Success<CheckDuplicationResponse> -> {
                    _isDuplicate.value = value.data.isExist
                    return@async 1
                }
                is Resource.Error -> {
                    Log.e("checkDuplication", "checkDuplication: ${value.errorMessage}")
                    return@async 0
                }
            }
        }.await()

    fun updateUserInfo(age: Int, gender: String, intro: String, nickname: String) {
        viewModelScope.launch {
            updateUserInfoUseCase(age, gender, intro, nickname)
        }
    }

    fun returnDuplicationTrue() {
        _isDuplicate.value = true
    }

    fun checkSurvey(position: Int, record: Int) {
        val userTest = userTest.value ?: return
        when (position) {
            0 -> userTest.preferNatureThanCity = record
            1 -> userTest.preferPlan = record
            2 -> userTest.preferDirectFlight = record
            3 -> userTest.preferCheapHotelThanComfort = record
            4 -> userTest.preferGoodFood = record
            5 -> userTest.preferCheapTraffic = record
            6 -> userTest.preferPersonalBudget = record
            7 -> userTest.preferTightSchedule = record
            8 -> userTest.preferShoppingThanTour = record
        }
        _userTest.value = userTest
    }

    fun updatePreferences(
        preferCheapHotelThanComfort: Int,
        preferCheapTraffic: Int,
        preferDirectFlight: Int,
        preferGoodFood: Int,
        preferNatureThanCity: Int,
        preferPersonalBudget: Int,
        preferPlan: Int,
        preferShoppingThanTour: Int,
        preferTightSchedule: Int
    ) {
        viewModelScope.launch {
            updatePreferencesUseCase(
                preferCheapHotelThanComfort,
                preferCheapTraffic,
                preferDirectFlight,
                preferGoodFood,
                preferNatureThanCity,
                preferPersonalBudget,
                preferPlan,
                preferShoppingThanTour,
                preferTightSchedule
            )
        }
    }

    fun updateBackgroundImg() {
        if (isChanged.value!!) {
            viewModelScope.launch {
                when (val value = updateBackgroundImgUseCase(backgroundFileMultiPart)) {
                    is Resource.Success<BackgroundImg> -> {
                        getUserProfile()
                        _isChanged.value = false
                    }
                    is Resource.Error -> {
                        Log.d("updateBackgroundImg", "updateBackgroundImg: ${value.errorMessage}")
                    }
                }
            }
        }
    }

    fun setBackgroundFile(uri: Uri, file: File) {
        viewModelScope.launch {
            _isChanged.value = true
            _backgroundImg.value = uri
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            backgroundFileMultiPart =
                MultipartBody.Part.createFormData("backgroundImg", file.name, requestFile)
        }
    }

    fun updateProfileImg() {
        viewModelScope.launch {
            if (isChanged.value!!) {
                updateProfileImgUseCase(markerFileMultiPart, profileFileMultiPart)
                _isChanged.value = false
                getUserProfile()
            }
        }
    }

    fun setProfileFile(uri: Uri, rotatedFileName: String, requestFile: RequestBody) {
        viewModelScope.launch {
            _isChanged.value = true
            _profileImg.value = uri
            MultipartBody.Part.createFormData("profileImg", rotatedFileName, requestFile)
        }
    }

    fun setCertificateImg1(uri: Uri?, rotatedFileName: String, requestFile: RequestBody) {
        viewModelScope.launch {
            _certificateImg1.value = uri
            certificateFile1MultiPart =
                MultipartBody.Part.createFormData("trainImg1", rotatedFileName, requestFile)
        }
    }

    fun setCertificateImg2(uri: Uri?, rotatedFileName: String, requestFile: RequestBody) {
        viewModelScope.launch {
            _certificateImg2.value = uri
            certificateFile2MultiPart =
                MultipartBody.Part.createFormData("trainImg2", rotatedFileName, requestFile)
        }
    }

    fun setCertificateImg3(uri: Uri?, rotatedFileName: String, requestFile: RequestBody) {
        viewModelScope.launch {
            _certificateImg3.value = uri
            certificateFile3MultiPart =
                MultipartBody.Part.createFormData("trainImg3", rotatedFileName, requestFile)
        }
    }

    fun setMarkerImg(file: File) {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        markerFileMultiPart =
            MultipartBody.Part.createFormData("markerImg", file.name, requestFile)

        updateProfileImg()
    }

    fun getUserProfile() = viewModelScope.launch {
        when (val value = getUserProfileUseCase()) {
            is Resource.Success -> {
                _userProfile.value = value.data
                ApplicationClass.preferences.apply {
                    nickname = value.data.nickname
                    gender = value.data.gender
                    age = value.data.age.toString()
                    profileImgPath = value.data.profileImgPath
                    markerImgPath = value.data.markerImgPath
                }
            }
            is Resource.Error -> {
                Log.d("getUserProfile", "getUserProfile: ${value.errorMessage}")
            }
        }
    }

    fun getMyPosts() = viewModelScope.launch {
        when (val value = getMyPostsUseCase()) {
            is Resource.Success -> {
                _posts.value = value.data
            }
            is Resource.Error -> {
                Log.d("getMyPosts", "getMyPosts: ${value.errorMessage}")
            }
        }
    }

    fun certificateProfile() = viewModelScope.launch {
        certificateProfileUseCase(
            certificateFile1MultiPart,
            certificateFile2MultiPart,
            certificateFile3MultiPart,
            _userProfile.value?.profileImgPath!!,
            _userProfile.value?.nickname!!
        )
    }

    fun logout() = viewModelScope.launch { logoutUseCase() }

    fun postNickname(nickname: String?) {
        _nickname.value = nickname
    }

    fun postAge(age: String?) {
        _age.value = age
    }

    fun postGender(gender: String?) {
        _gender.value = gender
    }

    fun postIntro(intro: String?) {
        _intro.value = intro
    }
}