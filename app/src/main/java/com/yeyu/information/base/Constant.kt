package com.yeyu.information.base

import android.support.multidex.BuildConfig

object Constant {

    //调试模式
    @JvmStatic
    val APP_DEBUG = BuildConfig.DEBUG

    const val DEFAULT_PAGE_SIZE = 10

    const val SERVER_URL = "http://campusinfo.shop:8080"

    const val BASE_URL = SERVER_URL + "/api/"

    //密钥
    const val APP_SECRET = "c37Am244P33u0638S563"

    //图片地址
    fun fillImgUrl(url: String): String? {
        var realUrl: String? = null
        if(url.isNotEmpty()){
            realUrl = if(url.startsWith("http://") || url.startsWith("https://")) {
                url
            } else {
                "$SERVER_URL/images/$url"
            }
        }
        return realUrl
    }


}