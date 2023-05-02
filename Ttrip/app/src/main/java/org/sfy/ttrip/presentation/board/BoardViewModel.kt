package org.sfy.ttrip.presentation.board

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.board.BoardBrief
import org.sfy.ttrip.domain.usecase.board.GetBoardBriefUseCase
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val getBoardBriefUseCase: GetBoardBriefUseCase
) : ViewModel() {

    private val _boardListData: MutableLiveData<List<BoardBrief>?> =
        MutableLiveData()
    val boardListData: LiveData<List<BoardBrief>?> = _boardListData


    fun getBoards(condition: Int, nation: String, city: String, keyword: String) =
        viewModelScope.launch {
            when (val value = getBoardBriefUseCase(condition, nation, city, keyword)) {
                is Resource.Success<List<BoardBrief>> -> {
                    _boardListData.value = value.data
                }
                is Resource.Error -> {
                    Log.e("getBoards", "getBoards: ${value.errorMessage}")
                }
            }
        }
}