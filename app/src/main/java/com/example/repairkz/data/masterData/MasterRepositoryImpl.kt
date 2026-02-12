package com.example.repairkz.data.masterData

import com.example.repairkz.common.models.Master
import javax.inject.Inject

class MasterRepositoryImpl @Inject constructor() : MasterRepository{
    override suspend fun fetchMasterById(id: Int): Result<Master> {
        return if (id == 67) {
            val master = Master(
                67,
                "Электрик",
                "Илья",
                null,
                3.5,
                "Починка - 1500 тг, диагностика - 800 тг, уборка - 4500 тг"
            )
            Result.success(master)
        } else {
            Result.failure(Exception("Мастер с id $id не найден"))
        }

    }
}