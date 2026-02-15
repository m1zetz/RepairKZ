package com.example.repairkz.data.masterData

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MasterRepositoryImpl @Inject constructor() : MasterRepository {

    private val _masters = MutableStateFlow<List<Master>?>(null)
    override val masters = _masters.asStateFlow()

    override suspend fun getMasters(): Result<List<Master>> {
        val mastersList = listOf(
            Master(
                id = 67,
                masterSpecialization = MasterSpetializationsEnum.ELECTRIC,
                masterName = "Илья",
                avatarURL = null,
                experienceInYears = 3,
                description = "Починка - 1500 тг, диагностика - 800 тг, уборка - 4500 тг",
                city = CitiesEnum.KARAGANDY,
                StatusOfUser.MASTER
            ),
            Master(
                id = 2,
                masterSpecialization = MasterSpetializationsEnum.PLUMBER,
                masterName = "Арман",
                avatarURL = null,
                experienceInYears = 10,
                description = "Устранение засоров, замена смесителя, установка ванн. Быстро и качественно.",
                city = CitiesEnum.ASTANA,
                StatusOfUser.MASTER
            ),
            Master(
                id = 3,
                masterSpecialization = MasterSpetializationsEnum.FURNITURE,
                masterName = "Дмитрий",
                avatarURL = null,
                experienceInYears = 5,
                description = "Покраска стен, обои, ламинат. Опыт работы в новостройках.",
                city = CitiesEnum.ALMATY,
                StatusOfUser.MASTER
            ),
            Master(
                id = 4,
                masterSpecialization = MasterSpetializationsEnum.ELECTRIC,
                masterName = "Сергей",
                avatarURL = null,
                experienceInYears = 1,
                description = "Замена розеток и выключателей. Недорого.",
                city = CitiesEnum.KARAGANDY,
                StatusOfUser.MASTER
            ),
            Master(
                id = 5,
                masterSpecialization = MasterSpetializationsEnum.PLUMBER,
                masterName = "Александр",
                avatarURL = null,
                experienceInYears = 7,
                description = "Монтаж отопления и водопровода любой сложности.",
                city = CitiesEnum.SHYMKENT,
                StatusOfUser.MASTER
            ),
            Master(
                id = 6,
                masterSpecialization = MasterSpetializationsEnum.СOMPUTER,
                masterName = "Берик",
                avatarURL = null,
                experienceInYears = 12,
                description = "Профессиональная укладка плитки и керамогранита. Гарантия 2 года.",
                city = CitiesEnum.ASTANA,
                StatusOfUser.MASTER
            )
        )
        _masters.value = mastersList
        return Result.success(mastersList)
    }

    override suspend fun fetchMasterById(id: Int): Result<Master> {
        if (_masters.value == null) {
            getMasters()
        }

        val master = _masters.value?.find { it.id == id }
        return if (master != null) {
            Result.success(master)
        } else {
            Result.failure(Exception("Мастер с id $id не найден"))
        }
    }
}