package com.yeyu.information.data

import com.google.gson.annotations.SerializedName
import com.yeyu.information.base.data.BaseResponse

data class CommonResponse(
    @field:SerializedName("success") override val success: String,
    @field:SerializedName("tips") override val tips: String,
) : BaseResponse()