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

    private val _userBirthDay: MutableLiveData<Boolean?> = MutableLiveData()
    val userBirthDay: LiveData<Boolean?> = _userBirthDay

    private val _userSex: MutableLiveData<Boolean?> = MutableLiveData()
    val userSex: LiveData<Boolean?> = _userSex

    private val _userIntro: MutableLiveData<Boolean?> = MutableLiveData()
    val userIntro: LiveData<Boolean?> = _userIntro

    private val _profileImgUri: MutableLiveData<Uri?> = MutableLiveData(null)
    val profileImgUri: MutableLiveData<Uri?> = _profileImgUri

    private var profileImgMultiPart: MultipartBody.Part? = null

    fun checkNickName() {
        _nickNameValid.value = !_nickNameValid.value!!
    }

    fun setProfileImg(uri: Uri, file: File) {
        viewModelScope.launch {
            _profileImgUri.value = uri
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            profileImgMultiPart =
                MultipartBody.Part.createFormData("profileImg", file.name, requestFile)
        }
    }
}