package com.yeyu.information.net

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yeyu.information.base.Constant
import com.yeyu.information.data.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

/**
 * 网络请求
 */
interface InformationService {

    /**
     * 密码登录
     * @param phone
     * @param pwd
     * @return
     */
    @GET("pwdLogin")
    suspend fun pwdLogin(
        @Query("phoneNumber") phone: String,
        @Query("pwd") pwd: String
    ): UserResponse

    /**
     * 发送验证码
     * @param phone
     * @param flag
     * @return
     */
    @GET("sendSmsCode")
    suspend fun sendSmsCode(
        @Query("phoneNumber") phone: String,
        @Query("flag") flag: String
    ) : CommonResponse

    /**
     * 验证码登录，未注册的号码，验证通过后将进行注册
     * @param phone
     * @param smsCode 验证码
     * @return
     */
    @GET("smsLogin")
    suspend fun smsLogin(
        @Query("phoneNumber") phone: String,
        @Query("smsCode") smsCode: String
    ) : UserResponse

    /**
     * 获取日报列表
     * @param page
     * @param pageSize
     * @param infoType 类型
     * @param keyword 搜索关键字
     */
    @GET("getInfo")
    suspend fun getInfo(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = Constant.DEFAULT_PAGE_SIZE,
        @Query("infoType") infoType: String,
        @Query("keyword") keyword: String
    ) : InfoListResponse

    /**
     * 获取日报详情
     * @param infoId
     */
    @GET("getInfoDetail")
    suspend fun getInfoDetail(
        @Query("infoId") infoId: String
    ) : InfoResponse

    /**
     * 点赞操作
     */
    @GET("executeLike")
    suspend fun executeLike(
        @Query("infoId") infoId: String,
        @Query("userId") userId: String,
        @Query("token") token: String
    ) : ResponseBody

    @GET("getInfoLike")
    suspend fun getLikeState(
        @Query("infoId") infoId: String,
        @Query("userId") userId: String,
        @Query("token") token: String
    ) : ResponseBody

    /**
     * 收藏操作
     */
    @GET("executeCollect")
    suspend fun executeCollect(
        @Query("infoId") infoId: String,
        @Query("userId") userId: String,
        @Query("token") token: String
    ) : ResponseBody

    @GET("getInfoCollect")
    suspend fun getCollectState(
        @Query("infoId") infoId: String,
        @Query("userId") userId: String,
        @Query("token") token: String
    ) : ResponseBody

    /**
     * 评论
     */
    @GET("getCommentList")
    suspend fun getCommentList(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = Constant.DEFAULT_PAGE_SIZE,
        @Query("infoId") infoId: String,
    ) : CommentListResponse

    @GET("insertComment")
    suspend fun publishComment(
        @Query("userId") userId: Int,
        @Query("infoId") infoId: String,
        @Query("content") content: String,
        @Query("publishTime") publishTime: String,
        @Query("token") token: String
    ) : ResponseBody

    /**
     * 上传文件
     */
    @Multipart
    @POST("uploadFile")
    suspend fun uploadFile(@Part parts: List<MultipartBody.Part>): PicMsgResponse

    @GET("modifyUser")
    suspend fun modifyUserMsg(
        @Query("phoneNumber") phoneNumber: String,
        @Query("username") username: String,
        @Query("gender") gender: String,
        @Query("birthday") birthday: String,
        @Query("headPicAddress") headPicAddress: String,
        @Query("token") token: String
    ) : ResponseBody

    @GET("getUser")
    suspend fun getUser(
        @Query("phoneNumber") phone: String,
        @Query("token") token: String
    ): UserResponse


    companion object {

        fun create(): InformationService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .connectTimeout(5,TimeUnit.SECONDS)
                .writeTimeout(5,TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
                .addInterceptor(logger)
                .addInterceptor(CustomerInterceptor())
                .build()

            val gson: Gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls()
                .create()

            return Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(InformationService::class.java)
        }
    }
}