package org.sfy.ttrip.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sfy.ttrip.domain.usecase.mypage.UpdateUserInfoUseCase
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val updateUserInfoUseCase: UpdateUserInfoUseCase
) : ViewModel() {

    var genderState = ""

    fun updateUserInfo(age: Int, gender: String, intro: String, nickname: String) {
        viewModelScope.launch {
            updateUserInfoUseCase(age, gender, intro, nickname)
        }
    }
}