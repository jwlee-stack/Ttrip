package org.sfy.ttrip.presentation.landmark

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.sfy.ttrip.domain.usecase.landmark.CreateDoodleUseCase
import java.io.File
import javax.inject.Inject

@HiltViewModel
class LandmarkViewModel @Inject constructor(
    private val createDoodleUseCase: CreateDoodleUseCase
) : ViewModel() {

    private val _positionX: MutableLiveData<Double?> = MutableLiveData()
    private val _positionY: MutableLiveData<Double?> = MutableLiveData()
    private val _positionZ: MutableLiveData<Double?> = MutableLiveData()

    private var doodleImgMultiPart: MultipartBody.Part? = null

    fun setDoodleImg(file: File) {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        doodleImgMultiPart =
            MultipartBody.Part.createFormData("doodleImgPath", file.name, requestFile)
    }

    fun createDoodle(landmarkId: Int, latitude: Double, longitude: Double) = viewModelScope.launch {
        createDoodleUseCase(
            _positionX.value!!,
            _positionY.value!!,
            _positionZ.value!!,
            doodleImgMultiPart,
            landmarkId,
            latitude,
            longitude
        )
    }

    fun setPositionX(num: Double) {
        _positionX.value = num
    }

    fun setPositionY(num: Double) {
        _positionY.value = num
    }

    fun setPositionZ(num: Double) {
        _positionZ.value = num
    }
}