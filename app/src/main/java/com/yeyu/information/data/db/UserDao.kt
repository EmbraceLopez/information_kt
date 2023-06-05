package com.yeyu.information.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yeyu.information.data.User
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the User class.
 */
@Dao
interface UserDao {

    //登录成功后插入用户信息
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userList: List<User>)

    //1、网络状态异常时查询缓存数据
    //2、登录状态检验
    @Query("SELECT * FROM t_user ")
    fun queryUser(): List<User>

    //退出登录后删除用户信息
    @Query("DELETE FROM t_user")
    suspend fun clearUser()

}