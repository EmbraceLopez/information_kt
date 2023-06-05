package com.yeyu.information.data

import com.google.gson.annotations.SerializedName

data class Comment(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("userId") val userId: String,
    @field:SerializedName("infoId") val infoId: String,
    @field:SerializedName("content") val content: String,
    @field:SerializedName("publishTime") val publishTime: String,
    @field:SerializedName("username") val username: String,
    @field:SerializedName("headPicAddress") val headPicAddress: String,
)
