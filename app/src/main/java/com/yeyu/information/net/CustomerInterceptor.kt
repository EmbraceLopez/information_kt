package com.yeyu.information.net

import com.yeyu.information.base.Constant
import com.yeyu.information.util.SignUtils
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CustomerInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val httpUrl: HttpUrl?
        var sign = ""
        val deviceId = ""

        try {
            sign = SignUtils.MD5(deviceId + Constant.APP_SECRET)
        } catch (e: Exception){
            e.printStackTrace()
        }

        httpUrl = request.url.newBuilder()
            .addQueryParameter("deviceId",deviceId)
            .addQueryParameter("sign",sign)
            .build()

        //Log.i("httpurl---",httpUrl.toString())

        request = request.newBuilder().url(httpUrl).build()

        return chain.proceed(request)
     }
}