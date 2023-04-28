package org.sfy.ttrip.presentation.init

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sfy.ttrip.domain.usecase.auth.SignUpUseCase
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    var verifyPhoneSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var passwordMatch: MutableLiveData<Boolean> = MutableLiveData(false)

    fun requestSignUp(phoneNumber: String, password: String) {
        viewModelScope.launch { signUpUseCase(phoneNumber, password) }
    }
}