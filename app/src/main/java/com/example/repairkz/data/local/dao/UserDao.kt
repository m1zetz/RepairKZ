package com.example.repairkz.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.repairkz.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun saveUser(userEntity: UserEntity)

    @Update
    suspend fun updateUserPhoto(userEntity: UserEntity)

    @Delete
    suspend fun clearUser(userEntity: UserEntity)

    @Query("SELECT * FROM user")
    fun getUser() : Flow<UserEntity?>
}