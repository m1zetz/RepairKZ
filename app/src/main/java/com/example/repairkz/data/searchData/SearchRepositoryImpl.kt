package com.example.repairkz.data.searchData

import com.example.repairkz.common.models.Master
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor() : SearchRepository {
    override suspend fun getMasters(): List<Master> {
        val master = Master(
            "Электрик",
            "Илья",
            null,
            3.5,
            "Починка - 1500 тг" +
                    "диагностика - 800 тг" +
                    "уборка - 4500 тг"
        )
        return listOf(master)
    }
}