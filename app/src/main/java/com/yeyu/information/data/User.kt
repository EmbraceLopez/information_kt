package com.yeyu.information.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * user表
 */
@Entity(tableName = "t_user")
data class User(
    @PrimaryKey @field:SerializedName("id") val id: Int,
    @field:SerializedName("phoneNumber") val phoneNumber: String,
    @field:SerializedName("pwd") var pwd: String,
    @field:SerializedName("username") val username: String,
    @field:SerializedName("smsCode") var smsCode: String,
    @field:SerializedName("sendTime") var sendTime: String,
    @field:SerializedName("gender") val gender: String,
    @field:SerializedName("birthday") val birthday: String,
    @field:SerializedName("headPicAddress") val headPicUrl: String,
    @field:SerializedName("token") var token: String,
)
