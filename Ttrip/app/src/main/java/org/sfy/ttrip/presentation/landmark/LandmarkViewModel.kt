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
import javax.inject.Inject

@HiltViewModel
class LandmarkViewModel @Inject constructor(
    private val getBadgesUseCase: GetBadgesUseCase
) : ViewModel() {

    private val _badges: MutableLiveData<List<BadgeItem>?> = MutableLiveData()
    val badges: LiveData<List<BadgeItem>?> = _badges

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
}