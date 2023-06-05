package com.yeyu.information.data

import com.google.gson.annotations.SerializedName
import com.yeyu.information.base.data.BaseResponse
import com.yeyu.information.data.Info

data class InfoResponse(
    @field:SerializedName("success") override val success: String,
    @field:SerializedName("tips") override val tips: String,
    @field:SerializedName("result") val result: Info
) : BaseResponse()