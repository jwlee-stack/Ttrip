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
import org.sfy.ttrip.domain.entity.board.BoardDetail
import org.sfy.ttrip.domain.usecase.board.DeleteBoardUseCase
import org.sfy.ttrip.domain.usecase.board.FinishBoardUseCase
import org.sfy.ttrip.domain.usecase.board.GetBoardBriefUseCase
import org.sfy.ttrip.domain.usecase.board.GetBoardDetailUseCase
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val getBoardBriefUseCase: GetBoardBriefUseCase,
    private val getBoardDetailUseCase: GetBoardDetailUseCase,
    private val deleteBoardUseCase: DeleteBoardUseCase,
    private val finishBoardUseCase: FinishBoardUseCase
) : ViewModel() {

    private val _boardListData: MutableLiveData<List<BoardBrief>?> =
        MutableLiveData()
    val boardListData: LiveData<List<BoardBrief>?> = _boardListData

    private val _boardData: MutableLiveData<BoardDetail?> = MutableLiveData()
    val boardData: LiveData<BoardDetail?> = _boardData

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

    fun getBoardDetail(boardId: Int) {
        viewModelScope.launch {
            when (val value = getBoardDetailUseCase(boardId)) {
                is Resource.Success<BoardDetail> -> {
                    _boardData.value = value.data
                }
                is Resource.Error -> {
                    Log.e("getBoardDetail", "getBoardDetail: ${value.errorMessage}")
                }
            }
        }
    }

    fun deleteBoard(boardId: Int) {
        viewModelScope.launch {
            deleteBoardUseCase.invoke(boardId)
        }
    }

    fun finishBoard(boardId: Int){
        viewModelScope.launch {
            finishBoardUseCase.invoke(boardId)
        }
    }
}