package org.sfy.ttrip.presentation.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor() : ViewModel() {

    private val _nickNameValid: MutableLiveData<Boolean?> = MutableLiveData()
    val nickNameValid: LiveData<Boolean?> = _nickNameValid

    fun checkNickName() {
        _nickNameValid.value = true
    }
}