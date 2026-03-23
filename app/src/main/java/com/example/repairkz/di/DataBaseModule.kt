package com.example.repairkz.di

import android.content.Context
import androidx.room.Room
import com.example.repairkz.data.local.dao.MasterDao
import com.example.repairkz.data.local.dao.UserDao
import com.example.repairkz.data.local.dataBase.RepairDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {
    @Provides
    fun provideDataBase(@ApplicationContext context: Context) : RepairDataBase{
        return Room.databaseBuilder(
            context,
            RepairDataBase::class.java,
            "repair_db"
        ).fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    fun provideUserDao(dataBase: RepairDataBase) : UserDao{
        return dataBase.userDao
    }
    @Provides
    fun provideMasterDao(dataBase: RepairDataBase) : MasterDao {
        return dataBase.masterDao
    }
}