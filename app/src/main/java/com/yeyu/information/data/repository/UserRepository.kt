package com.yeyu.information.data.repository

import com.yeyu.information.base.MyApplication
import com.yeyu.information.data.PicMsg
import com.yeyu.information.data.User
import com.yeyu.information.data.db.UserDao
import com.yeyu.information.net.InformationService
import com.yeyu.information.util.BitmapUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * 数据实体类
 * 1、向viewModel提供单一的数据源
 * 2、对于网络请求该类继承了BaseRepository，调用父类方法preProcessData现进行数据预处理
 */
class UserRepository @Inject constructor(
    private val service: InformationService,
    private val userDao: UserDao,
) : BaseRepository() {

    /**
     * 密码登录
     */
    suspend fun pwdLogin(phoneNumber: String,pwd: String): Flow<User> {
        return flow {
            val response = service.pwdLogin(phoneNumber,pwd)
            val result = preProcessData(response).result
            result.token = response.token
            //保存用户信息至数据库
            val userList: List<User> = listOf(result)
            userDao.clearUser()
            userDao.insertUser(userList)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取验证码
     */
    suspend fun sendSmsCode(phoneNumber: String,flag: String): Flow<Boolean> {
        return flow {
            val response = service.sendSmsCode(phoneNumber,flag)
            preProcessData(response)
            emit(true)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 验证码登录
     */
    suspend fun smsLogin(phoneNumber: String,smsCode: String): Flow<User> {
        return flow {
            val response = service.smsLogin(phoneNumber,smsCode)
            val result = preProcessData(response).result
            val userList: List<User> = listOf(result)
            userDao.clearUser()
            userDao.insertUser(userList)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取用户信息
     */
    suspend fun getUser(): Flow<List<User>> {
        return flow {
            emit(userDao.queryUser())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun uploadPic(paths: List<String>): Flow<List<PicMsg>> {
        val part = compress(paths)
        return flow<List<PicMsg>> {
            val response = service.uploadFile(part)
            emit(response.result)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 压缩图片
     */
    suspend fun compress(paths: List<String>): List<MultipartBody.Part> {
        return withContext(Dispatchers.IO) {
            val fileType = ".jpg"
            paths.map {
                val file = File(MyApplication.getApp().cacheDir, UUID.randomUUID().toString() + fileType)
                BitmapUtil.compressBitmapAndSave(
                    it, file.absolutePath, 1080, 1080, 500, true
                )
                val requestBody =
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("files", file.name, requestBody)
            }.toList()
        }
    }

    //修改用户信息
    suspend fun modifyUserMsg(username: String,gender: String,birthday: String,headPicAddress: String): Flow<Boolean> {
        return flow<Boolean> {
            userDao.queryUser().forEach { user ->
                val response = service.modifyUserMsg(user.phoneNumber,username,gender,birthday,headPicAddress,user.token)
                emit(JSONObject(response.string()).optBoolean("success"))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 退出登录
     * 删除用户信息
     */
    suspend fun clearUser(): Flow<Boolean> {
        return flow {
            userDao.clearUser()
            emit(true)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 重新获取用户信息
     */
    suspend fun getUserByPhoneNumber(): Flow<User> {
        return flow<User> {
            userDao.queryUser().forEach { user ->
                val response = service.getUser(user.phoneNumber,user.token)
                val result = response.result
                result.token = user.token
                val userList: List<User> = listOf(result)
                userDao.clearUser()
                userDao.insertUser(userList)
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

}