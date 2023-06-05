package com.yeyu.information.data

import com.google.gson.annotations.SerializedName
import com.yeyu.information.base.data.BaseResponse

data class CommentListResponse(
    @field:SerializedName("success") override val success: String,
    @field:SerializedName("tips") override val tips: String,
    @field:SerializedName("result") val result: List<Comment>
) : BaseResponse()