package com.yeyu.information.data.repository

import com.yeyu.information.base.MyApplication
import com.yeyu.information.data.Comment
import com.yeyu.information.data.Info
import com.yeyu.information.data.InfoListResponse
import com.yeyu.information.data.db.InfoDao
import com.yeyu.information.data.db.UserDao
import com.yeyu.information.net.InformationService
import com.yeyu.information.util.NetWorkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import javax.inject.Inject

/**
 * information数据源
 * 使用flow可进行流式操作，可接收所有通知。
 */
class InfoRepository @Inject constructor(
    private val service: InformationService,
    private val infoDao: InfoDao,
    private val userDao: UserDao
) : BaseRepository() {

    /**
     * 测试方法
     * 不应将fragment的上下文传递至viewModel，因为它们生命周期不同，可能导致内存泄漏
     */
    suspend fun getInfoList1(page: Int,infoType: String,keyword: String): Flow<List<Info>> {
        return flow {
            var result: List<Info> = listOf()
            //判断网络状态
            if(NetWorkUtil.isNetWorkAvailable(MyApplication.getApp().applicationContext)) {  //应使用application中的context
                //网络可用
                val response = service.getInfo(page = page,infoType = infoType, keyword = keyword)
                result = preProcessData(response).result
                //缓存第一页至数据库
                if(page == 1) {
                    infoDao.clearInfoList()
                    infoDao.insertAll(result)
                }
            } else {
                //网络不可用，则从数据库中取出第一页数据
                if(page == 1) {
                    result = infoDao.queryInfoByKeyword()
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * @param flag 标记刷新，用户刷新时，应进行网络请求最新的数据，而不是取数据库中的缓存数据。
     * 问题：打开应用，在没有网络的情况下，可以加载缓存数据，但有网络时，无法请求最新数据。
     * 解决：统一先加载数据库中的缓存数据，然后再请求网络，将最新的数据更新至数据库，然后添加到适配器中。
     */
    suspend fun getInfoList(page: Int,infoType: String,keyword: String,flag: Int): Flow<List<Info>> {
        return flow {
            var result: List<Info>
            val response: InfoListResponse
            if(page == 1) {
                result = infoDao.queryInfoByKeyword()
                if(result.isEmpty() || flag == 1) {
                    response = service.getInfo(page = page,infoType = infoType, keyword = keyword)
                    result = preProcessData(response).result
                    infoDao.clearInfoList()
                    infoDao.insertAll(result)
                }
            } else {
                response = service.getInfo(page = page,infoType = infoType, keyword = keyword)
                result = preProcessData(response).result
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 搜索
     */
    suspend fun searchInfoList(page: Int,infoType: String,keyword: String): Flow<List<Info>> {
        return flow<List<Info>> {
            val response = service.getInfo(page = page,infoType = infoType, keyword = keyword)
            emit(response.result)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取info详情
     */
    suspend fun getInfoDetail(infoId: String): Flow<Info> {
        return flow<Info> {
            val response = service.getInfoDetail(infoId)
            emit(response.result)
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 点赞
     */
    suspend fun executeLike(infoId: String): Flow<Int>{
        return flow {
            userDao.queryUser().forEach { user ->
                val response = service.executeLike(infoId,user.id.toString(),user.token)
                emit(JSONObject(response.string()).optInt("likeState"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLikeState(infoId: String): Flow<Int>{
        return flow {
            userDao.queryUser().forEach { user ->
                val response = service.getLikeState(infoId,user.id.toString(),user.token)
                emit(JSONObject(response.string()).optInt("likeState"))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 收藏
     */
    suspend fun executeCollect(infoId: String): Flow<Int>{
        return flow {
            userDao.queryUser().forEach { user ->
                val response = service.executeCollect(infoId,user.id.toString(),user.token)
                emit(JSONObject(response.string()).optInt("collectState"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCollectState(infoId: String): Flow<Int>{
        return flow {
            userDao.queryUser().forEach { user ->
                val response = service.getCollectState(infoId,user.id.toString(),user.token)
                emit(JSONObject(response.string()).optInt("collectState"))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 评论
     */
    suspend fun getCommentList(page: Int,infoId: String): Flow<List<Comment>> {
        return flow<List<Comment>> {
            val response = service.getCommentList(page = page, infoId = infoId)
            emit(response.result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun publishComment(infoId: String,content: String,publishTime: String): Flow<Boolean> {
        return flow<Boolean> {
            userDao.queryUser().forEach { user ->
                val response = service.publishComment(user.id,infoId,content,publishTime,user.token)
                emit(JSONObject(response.string()).optBoolean("success"))
            }
        }.flowOn(Dispatchers.IO)
    }

}