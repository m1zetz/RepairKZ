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
                userId = 67,
                userPhotoUrl = null,
                firstName = "Илья",
                lastName = "Иванов",
                email = "ilya.master@mail.kz",
                phoneNumber = "+77071112233",
                status = StatusOfUser.MASTER,
                city = CitiesEnum.KARAGANDY,
                experienceInYears = 3,
                description = "Починка - 1500 тг, диагностика - 800 тг, уборка - 4500 тг",
                masterSpecialization = MasterSpetializationsEnum.ELECTRIC
            ),
            Master(
                userId = 2,
                userPhotoUrl = null,
                firstName = "Арман",
                lastName = "Сериков",
                email = "arman.plumb@google.com",
                phoneNumber = "+77472223344",
                status = StatusOfUser.MASTER,
                city = CitiesEnum.ASTANA,
                experienceInYears = 10,
                description = "Устранение засоров, замена смесителя, установка ванн. Быстро и качественно.",
                masterSpecialization = MasterSpetializationsEnum.PLUMBER
            ),
            Master(
                userId = 3,
                userPhotoUrl = null,
                firstName = "Дмитрий",
                lastName = "Соколов",
                email = "dima.remont@yandex.kz",
                phoneNumber = "+77013334455",
                status = StatusOfUser.MASTER,
                city = CitiesEnum.ALMATY,
                experienceInYears = 5,
                description = "Покраска стен, обои, ламинат. Опыт работы в новостройках.",
                masterSpecialization = MasterSpetializationsEnum.FURNITURE
            ),
            Master(
                userId = 4,
                userPhotoUrl = null,
                firstName = "Сергей",
                lastName = "Ли",
                email = "sergey.electr@mail.ru",
                phoneNumber = "+77774445566",
                status = StatusOfUser.MASTER,
                city = CitiesEnum.KARAGANDY,
                experienceInYears = 1,
                description = "Замена розеток и выключателей. Недорого.",
                masterSpecialization = MasterSpetializationsEnum.ELECTRIC
            ),
            Master(
                userId = 5,
                userPhotoUrl = null,
                firstName = "Александр",
                lastName = "Петров",
                email = "alex.plumb@mail.kz",
                phoneNumber = "+77055556677",
                status = StatusOfUser.MASTER,
                city = CitiesEnum.SHYMKENT,
                experienceInYears = 7,
                description = "Монтаж отопления и водопровода любой сложности.",
                masterSpecialization = MasterSpetializationsEnum.PLUMBER
            ),
            Master(
                userId = 6,
                userPhotoUrl = null,
                firstName = "Берик",
                lastName = "Ахметов",
                email = "berik.it@gmail.com",
                phoneNumber = "+77006667788",
                status = StatusOfUser.MASTER,
                city = CitiesEnum.ASTANA,
                experienceInYears = 12,
                description = "Профессиональная укладка плитки и керамогранита. Гарантия 2 года.",
                masterSpecialization = MasterSpetializationsEnum.СOMPUTER
            )
        )
        _masters.value = mastersList
        return Result.success(mastersList)
    }

    override suspend fun fetchMasterById(id: Int): Result<Master> {
        if (_masters.value == null) {
            getMasters()
        }

        val master = _masters.value?.find { it.userId == id }
        return if (master != null) {
            Result.success(master)
        } else {
            Result.failure(Exception("Мастер с id $id не найден"))
        }
    }
}