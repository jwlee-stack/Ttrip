package org.sfy.ttrip.presentation.init

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor() : ViewModel() {

    private val _nickNameValid: MutableLiveData<Boolean> = MutableLiveData(false)
    val nickNameValid: LiveData<Boolean> = _nickNameValid

    private val _userAge: MutableLiveData<String?> = MutableLiveData("")
    val userAge: LiveData<String?> = _userAge

    private val _userSex: MutableLiveData<String?> = MutableLiveData("")
    val userSex: LiveData<String?> = _userSex

    private val _userIntro: MutableLiveData<String?> = MutableLiveData()
    val userIntro: LiveData<String?> = _userIntro

    private val _profileImgUri: MutableLiveData<Uri?> = MutableLiveData(null)
    val profileImgUri: MutableLiveData<Uri?> = _profileImgUri

    private var profileImgMultiPart: MultipartBody.Part? = null

    fun checkNickName() {
        _nickNameValid.value = !_nickNameValid.value!!
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
        }
    }
}