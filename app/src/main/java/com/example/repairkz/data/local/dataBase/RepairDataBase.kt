package com.example.repairkz.data.local.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.repairkz.data.local.dao.MasterDao
import com.example.repairkz.data.local.dao.UserDao
import com.example.repairkz.data.local.entity.MasterEntity
import com.example.repairkz.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, MasterEntity::class],
    version = 3
)
abstract class RepairDataBase : RoomDatabase(){
    abstract val userDao: UserDao

    abstract val masterDao: MasterDao

}