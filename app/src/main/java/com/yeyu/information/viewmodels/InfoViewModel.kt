package com.yeyu.information.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.yeyu.information.base.vm.BaseVm
import com.yeyu.information.data.Comment
import com.yeyu.information.data.Info
import com.yeyu.information.data.repository.InfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val infoRepository: InfoRepository
) : BaseVm() {

    private val _infoList = MutableLiveData<List<Info>>()
    val infoList: LiveData<List<Info>>
        get() = _infoList

    private val _searchInfoList = MutableLiveData<List<Info>>()
    val searchInfoList: LiveData<List<Info>>
        get() = _searchInfoList

    private val _infoDetail = MutableLiveData<Info>()
    val infoDetail: LiveData<Info>
        get() = _infoDetail

    private val _executeLikeState = MutableLiveData<Int>()
    val executeLikeState: LiveData<Int>
        get() = _executeLikeState

    private val _likeState = MutableLiveData<Int>()
    val likeState: LiveData<Int>
        get() = _likeState

    private val _executeCollectState = MutableLiveData<Int>()
    val executeCollectState: LiveData<Int>
        get() = _executeCollectState

    private val _collectState = MutableLiveData<Int>()
    val collectState: LiveData<Int>
        get() = _collectState

    private val _commentList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>>
        get() = _commentList

    private val _commentState = MutableLiveData<Boolean>()
    val commentState: LiveData<Boolean>
        get() = _commentState

    var paramInfoPage: Int = 1
    var paramSearchInfoPage: Int = 1
    var paramCommentPage: Int = 1

    fun getInfoList(page: Int,infoType: String,keyword: String,flag: Int) {
        paramInfoPage = page
        loadingLaunch(
            {
                infoRepository.getInfoList(page,infoType,keyword,flag)
                    .collectLatest {result ->
                        _infoList.value = result
                    }
            }
        )
    }

    fun searchInfoList(page: Int,infoType: String,keyword: String) {
        paramSearchInfoPage = page
        loadingLaunch(
            {
                infoRepository.searchInfoList(page,infoType,keyword)
                    .collectLatest {result ->
                        _searchInfoList.value = result
                    }
            }
        )
    }

    fun getInfoDetail(infoId: String) {
        launch(
            {
                infoRepository.getInfoDetail(infoId)
                    .collectLatest { result ->
                        _infoDetail.value = result
                    }
            }
        )
    }

    /**
     * 点赞
     */
    fun executeLike(infoId: String) {
        launch(
            {
                infoRepository.executeLike(infoId)
                    .collectLatest { result ->
                        _executeLikeState.value = result
                    }
            }
        )
    }

    fun getLikeState(infoId: String) {
        launch(
            {
                infoRepository.getLikeState(infoId)
                    .collectLatest { result ->
                        _likeState.value = result
                    }
            }
        )
    }

    /**
     * 收藏
     */
    fun executeCollect(infoId: String) {
        launch(
            {
                infoRepository.executeCollect(infoId)
                    .collectLatest { result ->
                        _executeCollectState.value = result
                    }
            }
        )
    }

    fun getCollectState(infoId: String) {
        launch(
            {
                infoRepository.getCollectState(infoId)
                    .collectLatest { result ->
                        _collectState.value = result
                    }
            }
        )
    }

    /**
     * 评论
     */
    fun getCommentList(page: Int,infoId: String) {
        paramCommentPage = page
        loadingLaunch(
            {
                infoRepository.getCommentList(page,infoId)
                    .collectLatest {result ->
                        _commentList.value = result
                    }
            }
        )
    }

    fun publishComment(infoId: String,content: String,publishTime: String) {
        loadingLaunch(
            {
                infoRepository.publishComment(infoId,content,publishTime)
                    .collectLatest { result ->
                        _commentState.value = result
                    }
            }
        )
    }


}