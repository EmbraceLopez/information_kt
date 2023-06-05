package com.yeyu.information.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.yeyu.information.base.vm.BaseVm
import com.yeyu.information.data.PicMsg
import com.yeyu.information.data.User
import com.yeyu.information.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * 使用liveData登录成功后会保留第一次user信息，
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : BaseVm() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private var _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?>
        get() = _userInfo

    private val _sendResult = MutableLiveData<Boolean>()
    val sendResult: LiveData<Boolean>
        get() = _sendResult

    private val _picMsgList = MutableLiveData<List<PicMsg>>()
    val picMsgList: LiveData<List<PicMsg>>
        get() = _picMsgList

    private val _modifyState = MutableLiveData<Boolean>()
    val modifyState: LiveData<Boolean>
        get() = _modifyState

    private val _loginOutState = MutableLiveData<Boolean>()
    val loginOutState: LiveData<Boolean>
        get() = _loginOutState

    fun getUser() =
        launch(
            {
                userRepository.getUser().collectLatest { result ->
                    if(result.isNotEmpty()) {
                        result.forEach { user ->
                            _userInfo.value = user
                        }
                    } else {
                        _userInfo.value = null
                    }
                }
            }
        )

    fun getUserInfo() =
        launch(
            {
                userRepository.getUser().collectLatest { result ->
                    result.forEach { user ->
                        _user.value = user
                    }
                }
            }
        )

    fun pwdLogin(phoneNumber: String,pwd: String) =
        loadingLaunch(
            {
                userRepository.pwdLogin(phoneNumber,pwd)
                    .collectLatest { result ->
                        _user.value = result
                    }
            }
        )

    fun sendSmsCode(phoneNumber: String,flag: String) =
        loadingLaunch(
            {
                userRepository.sendSmsCode(phoneNumber,flag)
                    .collectLatest { result ->
                        _sendResult.value = result
                    }
            }
        )

    fun smsLogin(phoneNumber: String,smsCode: String) =
        loadingLaunch(
            {
                userRepository.smsLogin(phoneNumber,smsCode)
                    .collectLatest { result ->
                        _user.value = result
                    }
            }
        )

    fun uploadPic(paths: List<String>) {
        if (paths.isEmpty())
            return
        loadingLaunch(
            {
                userRepository.uploadPic(paths)
                    .collectLatest { result ->
                        _picMsgList.value = result
                    }
            }
        )
    }

    fun modifyUserMsg(username: String,gender: String,birthday: String,headPicAddress: String) {
        loadingLaunch(
            {
                userRepository.modifyUserMsg(username,gender,birthday,headPicAddress)
                    .collectLatest { result ->
                        _modifyState.value = result
                    }
            }
        )
    }

    fun clearUser() {
        loadingLaunch(
            {
                delay(1000)
                userRepository.clearUser()
                    .collectLatest { result ->
                        _loginOutState.value = result
                    }
            }
        )
    }

    fun getUserByPhoneNumber() {
        loadingLaunch(
            {
                userRepository.getUserByPhoneNumber()
                    .collectLatest {result ->
                        _userInfo.value = result
                    }
            }
        )
    }




}