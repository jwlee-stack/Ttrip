package org.sfy.ttrip.presentation.board

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sfy.ttrip.data.remote.Resource
import org.sfy.ttrip.domain.entity.board.*
import org.sfy.ttrip.domain.entity.user.UserProfileDialog
import org.sfy.ttrip.domain.usecase.board.*
import org.sfy.ttrip.domain.usecase.user.GetUserProfileDialogUseCase
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val getBoardBriefUseCase: GetBoardBriefUseCase,
    private val getBoardDetailUseCase: GetBoardDetailUseCase,
    private val deleteBoardUseCase: DeleteBoardUseCase,
    private val finishBoardUseCase: FinishBoardUseCase,
    private val getBoardCommentUseCase: GetBoardCommentUseCase,
    private val postCommentUseCase: PostCommentUseCase,
    private val postBoardUseCase: PostBoardUseCase,
    private val getUserProfileDialogUseCase: GetUserProfileDialogUseCase,
    private val getRecommendUseCase: GetRecommendUseCase
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

    private val _postStartDate: MutableLiveData<String> = MutableLiveData(null)
    val postStartDate: MutableLiveData<String> = _postStartDate

    private val _postEndDate: MutableLiveData<String?> = MutableLiveData(null)
    val postEndDate: MutableLiveData<String?> = _postEndDate

    private val _userProfile: MutableLiveData<UserProfileDialog?> = MutableLiveData(null)
    val userProfile: MutableLiveData<UserProfileDialog?> = _userProfile

    private val _postBoardNation: MutableLiveData<String?> = MutableLiveData(null)
    val postBoardNation: MutableLiveData<String?> = _postBoardNation

    private val _postBoardCity: MutableLiveData<String?> = MutableLiveData(null)
    val postBoardCity: MutableLiveData<String?> = _postBoardCity

    private val _boardId: MutableLiveData<Int?> = MutableLiveData(null)
    val boardId: MutableLiveData<Int?> = _boardId

    private val _authorId: MutableLiveData<Int?> = MutableLiveData(null)
    val authorId: MutableLiveData<Int?> = _authorId

    private val _recommendBoardListData: MutableLiveData<List<RecommendBoard>?> = MutableLiveData()
    val recommendBoardListData: LiveData<List<RecommendBoard>?> = _recommendBoardListData

    fun getUserProfile(nickname: String) =
        viewModelScope.launch {
            when (val value = getUserProfileDialogUseCase(nickname)) {
                is Resource.Success<UserProfileDialog> -> {
                    _userProfile.value = value.data
                }
                is Resource.Error -> {
                    Log.e("getUserProfile", "getUserProfile: ${value.errorMessage}")
                }
            }
        }

    fun clearUserProfile() {
        _userProfile.value = null
    }

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

    fun postBoard() {
        viewModelScope.launch {
            when (val value = postBoardUseCase(
                _postBoardCity.value!!,
                _postBoardContent.value!!,
                _postEndDate.value!!,
                _postBoardNation.value!!,
                _postStartDate.value!!,
                _postBoardTitle.value!!
            )) {
                is Resource.Success<PostBoard> -> {
                    _authorId.value = value.data.authorId
                    _postBoardContent.value = value.data.content
                    _postBoardCity.value = value.data.city
                    _boardId.value = value.data.articleId
                }
                is Resource.Error -> {
                    Log.e("postBoard", "postBoard: ${value.errorMessage}")
                }
            }
        }
    }

    fun getRecommendBoard() {
        viewModelScope.launch {
            when (val value = getRecommendUseCase(
                PostBoard(
                    _boardId.value!!,
                    _postBoardCity.value!!,
                    _postBoardContent.value!!,
                    _authorId.value!!
                )
            )) {
                is Resource.Success<List<RecommendBoard>> -> {
                    Log.d("123123", "getRecommendBoard: ${value.data}")
                    _recommendBoardListData.value = value.data
                }
                is Resource.Error -> {
                    Log.e("getRecommendBoard", "getRecommendBoard: ${value.errorMessage}")
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

            delay(500)
            getBoardDetail(boardId)
        }
    }

    fun postBoardTitle(title: String?) {
        _postBoardTitle.value = title
    }

    fun postBoardContent(content: String?) {
        _postBoardContent.value = content
    }

    fun postBoardNation(nation: String?) {
        _postBoardNation.value = nation
    }

    fun postBoardCity(city: String?) {
        _postBoardCity.value = city
    }

    fun postStartDate(startDate: String?) {
        _postStartDate.value = startDate!!
    }

    fun postEndDate(endDate: String?) {
        _postEndDate.value = endDate
    }

    fun clearPostData() {
        _postBoardTitle.value = null
        _postBoardContent.value = null
        _postBoardNation.value = null
        _postBoardCity.value = null
    }
}