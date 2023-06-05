package com.yeyu.information.data.repository

import com.yeyu.information.base.data.BaseResponse

open class BaseRepository {

    //数据预处理
    open fun <T : BaseResponse> preProcessData(response: T): T {
        return if(response.success == "true") {
            response
        } else {
            val msg = response.tips ?: "服务器响应数据失败"
            throw Throwable(msg)
        }
    }

}