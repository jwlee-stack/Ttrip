package org.sfy.ttrip.presentation.landmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.landmark.BadgeItem
import org.sfy.ttrip.domain.usecase.landmark.GetBadgesUseCase
import org.sfy.ttrip.domain.usecase.landmark.IssueBadgeUseCase
import javax.inject.Inject

@HiltViewModel
class LandmarkViewModel @Inject constructor(
    private val getBadgesUseCase: GetBadgesUseCase,
    private val issueBadgeUseCase: IssueBadgeUseCase
) : ViewModel() {

    private val _badges: MutableLiveData<List<BadgeItem>?> = MutableLiveData()
    val badges: LiveData<List<BadgeItem>?> = _badges

    private val _issueStatus: MutableLiveData<Int?> = MutableLiveData()
    val issueStatus: LiveData<Int?> = _issueStatus

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
        _issueStatus.value = value
    }
}