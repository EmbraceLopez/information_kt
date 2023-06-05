package com.yeyu.information.data

import com.google.gson.annotations.SerializedName
import com.yeyu.information.base.data.BaseResponse

data class UserResponse(
    @field:SerializedName("success") override val success: String,
    @field:SerializedName("tips") override val tips: String,
    @field:SerializedName("result") val result: User,
    @field:SerializedName("token") val token: String
) : BaseResponse()

