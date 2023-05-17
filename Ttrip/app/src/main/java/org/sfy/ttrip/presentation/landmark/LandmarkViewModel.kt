package org.sfy.ttrip.presentation.landmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.landmark.DoodleItem
import org.sfy.ttrip.domain.usecase.landmark.CreateDoodleUseCase
import org.sfy.ttrip.domain.usecase.landmark.GetDoodlesUseCase
import java.io.File
import org.sfy.ttrip.domain.entity.landmark.BadgeItem
import org.sfy.ttrip.domain.usecase.landmark.GetBadgesUseCase
import org.sfy.ttrip.domain.usecase.landmark.IssueBadgeUseCase
import javax.inject.Inject

@HiltViewModel
class LandmarkViewModel @Inject constructor(
    private val createDoodleUseCase: CreateDoodleUseCase,
    private val getDoodlesUseCase: GetDoodlesUseCase,
    private val getBadgesUseCase: GetBadgesUseCase,
    private val issueBadgeUseCase: IssueBadgeUseCase
) : ViewModel() {

    private val _badges: MutableLiveData<List<BadgeItem>?> = MutableLiveData()
    val badges: LiveData<List<BadgeItem>?> = _badges

    private val _issueStatus: MutableLiveData<Int?> = MutableLiveData(0)
    val issueStatus: LiveData<Int?> = _issueStatus

    private val _doodles: MutableLiveData<List<DoodleItem>?> = MutableLiveData()
    val doodles: LiveData<List<DoodleItem>?> = _doodles

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

    fun getDoodles(landmarkId: Int) = viewModelScope.launch {
        when (val value = getDoodlesUseCase(landmarkId)) {
            is Resource.Success -> {
                _doodles.value = value.data
            }
            is Resource.Error -> {
                Log.e("getDoodles", "getDoodles: ${value.errorMessage}")
            }
        }
    }

    fun getBadges() = viewModelScope.launch {
        when (val value = getBadgesUseCase()) {
            is Resource.Success -> {
                _badges.value = value.data
            }
            is Resource.Error -> {
                Log.e("getBadges", "getBadges: ${value.errorMessage}")
            }
        }
    }

    fun issueBadge(landmarkId: Int) = viewModelScope.launch {
        val value = issueBadgeUseCase(landmarkId)
        _issueStatus.postValue(value)
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

    fun clearIssueStatus() {
        _issueStatus.value = null
    }
}