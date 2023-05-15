package org.sfy.ttrip.presentation.init

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sfy.ttrip.ApplicationClass
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.auth.AccessToken
import org.sfy.ttrip.domain.entity.auth.Auth
import org.sfy.ttrip.domain.usecase.auth.AccessTokenUseCase
import org.sfy.ttrip.domain.usecase.auth.LoginUseCase
import org.sfy.ttrip.domain.usecase.auth.SignUpUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val loginUseCase: LoginUseCase,
    private val accessTokenUseCase: AccessTokenUseCase
) : ViewModel() {

    private val _userData: MutableLiveData<Auth?> = MutableLiveData()
    val userData: LiveData<Auth?> = _userData

    private val _isValid: MutableLiveData<Boolean> = MutableLiveData(true)
    val isValid: LiveData<Boolean> = _isValid

    private val _isAutoLogin: MutableLiveData<Boolean> = MutableLiveData(true)
    val isAutoLogin: LiveData<Boolean> = _isAutoLogin

    private val _emptyNickname: MutableLiveData<Boolean?> = MutableLiveData()
    val emptyNickname: LiveData<Boolean?> = _emptyNickname

    var verifyPhoneSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var passwordMatch: MutableLiveData<Boolean> = MutableLiveData(false)

    fun requestAccessToken() = viewModelScope.launch {
        when (val value = accessTokenUseCase(
            ApplicationClass.preferences.accessToken.toString(),
            ApplicationClass.preferences.refreshToken.toString()
        )) {
            is Resource.Success<AccessToken> -> {
                if (value.data.grantType == null) {
                    _isAutoLogin.value = false
                } else {
                    ApplicationClass.preferences.accessToken = value.data.accessToken
                    ApplicationClass.preferences.refreshToken = value.data.refreshToken
                    _emptyNickname.value = value.data.nickname == null
                    _isAutoLogin.value = true
                }
            }
            is Resource.Error -> {

            }
        }
    }

    fun requestSignUp(phoneNumber: String, password: String) {
        viewModelScope.launch { signUpUseCase(phoneNumber, password) }
    }

    fun requestLogin(phoneNumber: String, password: String) = viewModelScope.launch {
        when (val value = loginUseCase(phoneNumber, password)) {
            is Resource.Success -> {
                _userData.value = value.data
                _emptyNickname.value = value.data.nickname == null
                ApplicationClass.preferences.accessToken = value.data.token.accessToken
                ApplicationClass.preferences.refreshToken = value.data.token.refreshToken
                ApplicationClass.preferences.userId = value.data.uuid
                ApplicationClass.preferences.nickname = value.data.nickname
                ApplicationClass.preferences.gender = value.data.gender
                ApplicationClass.preferences.age = value.data.age.toString()
                ApplicationClass.preferences.profileImgPath = value.data.profileImgPath
                ApplicationClass.preferences.markerImgPath = value.data.markerImgPath

                _isValid.value = true
            }
            is Resource.Error -> {
                Log.d("requestLogin", "requestLogin: ${value.errorMessage}")

                _isValid.value = false
            }
        }
    }

    fun makeIsValidTrue() {
        _isValid.value = true
    }

    fun clearEmptyNickname() {
        _emptyNickname.value = null
    }
}