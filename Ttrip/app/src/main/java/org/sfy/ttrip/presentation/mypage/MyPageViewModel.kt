package org.sfy.ttrip.presentation.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.data.remote.datasorce.user.CheckDuplicationResponse
import org.sfy.ttrip.domain.entity.mypage.UserProfile
import org.sfy.ttrip.domain.entity.user.UserTest
import org.sfy.ttrip.domain.usecase.mypage.GetUserProfileUseCase
import org.sfy.ttrip.domain.usecase.mypage.LogoutUseCase
import org.sfy.ttrip.domain.usecase.mypage.UpdatePreferencesUseCase
import org.sfy.ttrip.domain.usecase.mypage.UpdateUserInfoUseCase
import org.sfy.ttrip.domain.usecase.user.CheckDuplicationUseCase
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val updatePreferencesUseCase: UpdatePreferencesUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val checkDuplicationUseCase: CheckDuplicationUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

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

    fun getUserProfile() = viewModelScope.launch {
        when (val value = getUserProfileUseCase()) {
            is Resource.Success -> {
                _userProfile.value = value.data
            }
            is Resource.Error -> {
                Log.d("getUserProfile", "getUserProfile: ${value.errorMessage}")
            }
        }
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