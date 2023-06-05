package com.yeyu.information.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yeyu.information.data.Info
import kotlinx.coroutines.flow.Flow


@Dao
interface InfoDao {

    //缓存最新一页列表
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(infoList: List<Info>)

    //网络状态异常时查询缓存数据并显示
    @Query("SELECT * FROM t_info ")
    fun queryInfoByKeyword(): List<Info>

    //刷新时清空之前缓存数据
    @Query("DELETE FROM t_info")
    suspend fun clearInfoList()

}