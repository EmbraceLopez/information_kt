package com.yeyu.information.base.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.yeyu.information.data.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException

open class BaseVm : ViewModel() {

    open val loadState = MutableLiveData<LoadState>()

    /**
     * 协程加载方法
     */
    open fun launch(
        block: suspend CoroutineScope.() -> Unit,
        onError: (e: Throwable) -> Unit = {}
    ) {
        viewModelScope.launch(CoroutineExceptionHandler{ ctx, throwable -> onError(throwable) }) {
            try {
                block()
            } catch (e: Exception) {
                parseException(e)
            }
        }
    }

    open fun loadingLaunch(
        block1: suspend CoroutineScope.() -> Unit,
        block2: (() -> Unit)? = null
    ) {
        launch(
            {
                loadState.value = LoadState.Loading()
                block1()
                loadState.value = LoadState.Success()
            },
            {
                loadState.value =
                    LoadState.Fail(it.message ?: "服务器返回数据失败")
            }
        )
    }

    /**
     * 处理异常分类信息
     */
    private fun parseException(e: Exception) {
        e.printStackTrace()

        val errorInfo = when (e) {
            is HttpException -> { "服务器繁忙，请稍后再试" }
            is ConnectException -> { "网络连接失败，请检查网络" }
            is UnknownHostException -> { "网络连接失败，请检查网络" }
            is InterruptedIOException -> { "网络连接超时，请检查网络" }
            is JsonParseException -> { "解析服务器响应数据失败" }
            is JSONException -> { "解析服务器响应数据失败" }
            is ParseException -> { "解析服务器响应数据失败" }
            else -> { "未知错误，请稍后再试" }
        }

        throw Throwable(errorInfo)
    }
}