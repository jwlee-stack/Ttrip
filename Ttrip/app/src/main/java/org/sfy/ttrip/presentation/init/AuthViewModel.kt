package org.sfy.ttrip.presentation.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.user.Auth
import org.sfy.ttrip.domain.usecase.auth.LoginUseCase
import org.sfy.ttrip.domain.usecase.auth.SignUpUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _userData: MutableLiveData<Auth?> = MutableLiveData()
    val userData: LiveData<Auth?> = _userData

    var verifyPhoneSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var passwordMatch: MutableLiveData<Boolean> = MutableLiveData(false)

    fun requestSignUp(phoneNumber: String, password: String) {
        viewModelScope.launch { signUpUseCase(phoneNumber, password) }
    }

    fun requestLogin(phoneNumber: String, password: String) = viewModelScope.launch {
        when (val value = loginUseCase(phoneNumber, password)) {
            is Resource.Success(value) -> {
                _userData.value = value.data
            }
            is Resource.Error -> {

            }
        })
    }
}
