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
import org.sfy.ttrip.domain.entity.board.BoardComment
import org.sfy.ttrip.domain.entity.board.BoardDetail
import org.sfy.ttrip.domain.usecase.board.*
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val getBoardBriefUseCase: GetBoardBriefUseCase,
    private val getBoardDetailUseCase: GetBoardDetailUseCase,
    private val deleteBoardUseCase: DeleteBoardUseCase,
    private val finishBoardUseCase: FinishBoardUseCase,
    private val getBoardCommentUseCase: GetBoardCommentUseCase,
    private val postCommentUseCase: PostCommentUseCase
) : ViewModel() {

    private val _boardListData: MutableLiveData<List<BoardBrief>?> = MutableLiveData()
    val boardListData: LiveData<List<BoardBrief>?> = _boardListData

    private val _boardCommentListData: MutableLiveData<List<BoardComment>?> = MutableLiveData()
    val boardCommentListData: LiveData<List<BoardComment>?> = _boardCommentListData

    private val _boardData: MutableLiveData<BoardDetail?> = MutableLiveData()
    val boardData: LiveData<BoardDetail?> = _boardData

    private val _postBoardTitle: MutableLiveData<String?> = MutableLiveData(null)
    val postBoardTitle: MutableLiveData<String?> = _postBoardTitle

    private val _postBoardContent: MutableLiveData<String?> = MutableLiveData(null)
    val postBoardContent: MutableLiveData<String?> = _postBoardContent

    private val _postStartDate: MutableLiveData<String?> = MutableLiveData(null)
    val postStartDate: MutableLiveData<String?> = _postStartDate

    private val _postEndDate: MutableLiveData<String?> = MutableLiveData(null)
    val postEndDate: MutableLiveData<String?> = _postEndDate

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

    fun getBoardComment(boardId: Int) {
        viewModelScope.launch {
            when (val value = getBoardCommentUseCase(boardId)) {
                is Resource.Success<List<BoardComment>> -> {
                    _boardCommentListData.value = value.data
                }
                is Resource.Error -> {
                    Log.e("getBoardComment", "getBoardComment: ${value.errorMessage}")
                }
            }
        }
    }

    fun deleteBoard(boardId: Int) {
        viewModelScope.launch {
            deleteBoardUseCase.invoke(boardId)
        }
    }

    fun finishBoard(boardId: Int) {
        viewModelScope.launch {
            finishBoardUseCase.invoke(boardId)
        }
    }

    fun postComment(boardId: Int, content: String?) {
        viewModelScope.launch {
            postCommentUseCase.invoke(boardId, content!!)
        }
    }

    fun postBoardTitle(title: String?) {
        _postBoardTitle.value = title
    }

    fun postBoardContent(content: String?) {
        _postBoardContent.value = content
    }

    fun postStartDate(startDate: String?) {
        _postStartDate.value = startDate
    }

    fun postEndDate(endDate: String?) {
        _postEndDate.value = endDate
    }
}