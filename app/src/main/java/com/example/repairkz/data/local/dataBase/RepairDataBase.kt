package com.example.repairkz.data.local.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.repairkz.data.local.dao.MasterDao
import com.example.repairkz.data.local.dao.UserDao
import com.example.repairkz.data.local.entity.MasterEntity
import com.example.repairkz.data.local.entity.UserEntity
import retrofit2.Converter

@TypeConverters(Converters::class)
@Database(
    entities = [UserEntity::class, MasterEntity::class],
    version = 4
)
abstract class RepairDataBase : RoomDatabase(){
    abstract val userDao: UserDao

    abstract val masterDao: MasterDao

}