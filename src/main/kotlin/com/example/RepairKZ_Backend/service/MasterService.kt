package com.example.RepairKZ_Backend.service

import com.example.RepairKZ_Backend.entity.MasterServiceEntity
import com.example.RepairKZ_Backend.model.MasterServiceDTO
import com.example.RepairKZ_Backend.repository.MasterRepository
import com.example.RepairKZ_Backend.repository.MasterServiceRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class MasterService(
    val masterRepository: MasterRepository,
    val masterServiceRepository: MasterServiceRepository
) {
    fun getServicesByMasterId(masterId: Long): List<MasterServiceDTO> {
        return masterServiceRepository.findByMasterId(masterId).map {
            it.toDto()
        }
    }

    fun createService(dto: MasterServiceDTO) : MasterServiceDTO {
        val master = masterRepository.findByIdOrNull(dto.masterId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val service = MasterServiceEntity(
            master = master,
            service = dto.service,
            price = dto.price,
            position = dto.position,
        )
        val entity = masterServiceRepository.save(service)
        return dto.copy(
            id = entity.id
        )
    }

    @Transactional
    fun updateService(dto: MasterServiceDTO) : MasterServiceDTO {
        val service = masterServiceRepository.findByIdOrNull(dto.id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
       return  service.apply {
            service.service = dto.service
            service.price = dto.price
        }.toDto()
    }

    @Transactional
    fun deleteService(id : Long) {
        masterServiceRepository.deleteById(id)
    }

}