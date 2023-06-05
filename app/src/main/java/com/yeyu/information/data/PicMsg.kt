package com.yeyu.information.data

import com.google.gson.annotations.SerializedName

data class PicMsg(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("path") val path: String
)