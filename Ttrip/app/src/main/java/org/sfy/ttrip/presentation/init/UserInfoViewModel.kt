package org.sfy.ttrip.presentation.init

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
import okhttp3.RequestBody.Companion.asRequestBody
import org.sfy.ttrip.ApplicationClass
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.user.CheckDuplicationResponse
import org.sfy.ttrip.domain.entity.user.UserTest
import org.sfy.ttrip.domain.usecase.user.*
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val checkDuplicationUseCase: CheckDuplicationUseCase,
    private val postUserInfoUseCase: PostUserInfoUseCase,
    private val postUserInfoTestUseCase: PostUserInfoTestUseCase,
    private val postUserFcmTokenUseCase: PostUserFcmTokenUseCase,
    private val postEvaluateUserUseCase: PostEvaluateUserUseCase,
    private val postReportUserUseCase: PostReportUserUseCase
) : ViewModel() {

    private val _isDuplicate: MutableLiveData<Boolean?> = MutableLiveData(null)
    val isDuplicate: MutableLiveData<Boolean?> = _isDuplicate

    private val _nickname: MutableLiveData<String> = MutableLiveData(null)
    val nickname: MutableLiveData<String> = _nickname

    private val _userAge: MutableLiveData<String?> = MutableLiveData("")
    val userAge: LiveData<String?> = _userAge

    private val _userSex: MutableLiveData<String?> = MutableLiveData("")
    val userSex: LiveData<String?> = _userSex

    private val _userIntro: MutableLiveData<String?> = MutableLiveData()
    val userIntro: LiveData<String?> = _userIntro

    private val _profileImgUri: MutableLiveData<Uri?> = MutableLiveData(null)
    val profileImgUri: MutableLiveData<Uri?> = _profileImgUri

    private var profileImgMultiPart: MultipartBody.Part? = null
    var markerfile: File? = null
    private var markerImgMultiPart: MultipartBody.Part? = null

    private val _userTest: MutableLiveData<UserTest> =
        MutableLiveData(UserTest(0, 0, 0, 0, 0, 0, 0, 0, 0))
    val userTest: LiveData<UserTest> = _userTest

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

    fun postUserInfoTest() = viewModelScope.launch {
        postUserInfoTestUseCase(_userTest.value!!)
    }

    fun postUserInfo() = viewModelScope.launch {
        when (val value = postUserInfoUseCase(
            _nickname.value!!,
            _userIntro.value!!,
            _userSex.value!!,
            profileImgMultiPart,
            markerImgMultiPart,
            _userAge.value!!,
            ""
        )) {
            is Resource.Success -> {
                ApplicationClass.preferences.apply {
                    nickname = value.data.nickname
                    gender = value.data.gender
                    age = value.data.age.toString()
                    profileImgPath = value.data.profileImgPath
                    markerImgPath = value.data.markerImgPath
                }
            }
            is Resource.Error -> {
                Log.e("postUserInfo", "postUserInfo: ${value.errorMessage}")
            }
        }
    }

    fun changeDuplicationTrue() {
        _isDuplicate.value = true
    }

    fun postAge(age: String?) {
        _userAge.value = age
    }

    fun postSex(sex: String?) {
        _userSex.value = sex
    }

    fun postIntro(intro: String?) {
        _userIntro.value = intro
    }

    fun setProfileImg(uri: Uri, file: File) {
        viewModelScope.launch {
            _profileImgUri.value = uri
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            profileImgMultiPart =
                MultipartBody.Part.createFormData("profileImg", file.name, requestFile)

            markerfile = file
        }
    }

    fun setMarkerImg(file: File) {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        markerImgMultiPart =
            MultipartBody.Part.createFormData("markerImg", file.name, requestFile)
    }

    fun postUserFcmToken(alarm: Boolean, token: String) {
        if (alarm) viewModelScope.launch { postUserFcmTokenUseCase(token) }
        else viewModelScope.launch { postUserFcmTokenUseCase("") }
    }

    fun postEvaluateUser(matchHistoryId: String, rate: Int) {
        viewModelScope.launch {
            postEvaluateUserUseCase(matchHistoryId, rate)
        }
    }

    fun postReportUser(reportContext: String, reportedNickname: String) {
        viewModelScope.launch {
            postReportUserUseCase(reportContext, reportedNickname)
        }
    }
}