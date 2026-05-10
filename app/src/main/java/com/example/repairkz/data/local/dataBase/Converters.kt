package com.example.repairkz.data.local.dataBase

import androidx.room.TypeConverter
import com.example.repairkz.data.remote.dto.MasterServiceDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromServicesList(services: List<MasterServiceDTO>): String{
        return gson.toJson(services)
    }

    @TypeConverter
    fun toServicesList(json: String): List<MasterServiceDTO>{
        val type = object : TypeToken<List<MasterServiceDTO>>() {}.type
        return gson.fromJson(json,type)
    }
}