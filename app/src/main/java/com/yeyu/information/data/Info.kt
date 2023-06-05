package com.yeyu.information.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * info表
 */
@Entity(tableName = "t_info")
data class Info(
    @PrimaryKey @field:SerializedName("id") val id: Int,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("content") val content: String,
    @field:SerializedName("dateline") val dateline: String,
    @field:SerializedName("infoUrl") val infoUrl: String,
    @field:SerializedName("infoType") val infoType: String,
    @field:SerializedName("infoPicAddress") val infoPicUrl: String,
    @field:SerializedName("collectCount") val collectCount: Int,
    @field:SerializedName("likeCount") val likeCount: Int,
    @field:SerializedName("commentCount") val commentCount: Int,
    @field:SerializedName("glanceCount") val glanceCount: Int,
)
