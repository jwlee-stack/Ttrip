package org.sfy.ttrip.presentation.init

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    var verifyPhoneSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var passwordMatch: MutableLiveData<Boolean> = MutableLiveData(false)
}