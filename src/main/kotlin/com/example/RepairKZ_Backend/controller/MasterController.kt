package com.example.RepairKZ_Backend.controller

import com.example.RepairKZ_Backend.entity.Master
import com.example.RepairKZ_Backend.model.MasterResponseDTO
import com.example.RepairKZ_Backend.model.MasterServiceDTO
import com.example.RepairKZ_Backend.service.MasterService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/master")
class MasterController(
    val masterService: MasterService
) {

    @GetMapping("services/{id}")
    fun getServicesByMasterId(@PathVariable("id") masterId: Long): ResponseEntity<List<MasterServiceDTO>> {
        val response = masterService.getServicesByMasterId(masterId)
        return ResponseEntity.ok(response)
    }

    @PostMapping("create")
    fun createService(@RequestBody masterServiceDto: MasterServiceDTO) : ResponseEntity<MasterServiceDTO> {
        val response =  masterService.createService(masterServiceDto)
        return ResponseEntity.ok(response)
    }

    @PutMapping("update")
    fun updateService(@RequestBody masterServiceDto: MasterServiceDTO ) : ResponseEntity<MasterServiceDTO> {
        val response =  masterService.updateService(masterServiceDto)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("delete/{id}")
    fun deleteService(@PathVariable id: Long) : ResponseEntity<Unit> {
        val response = masterService.deleteService(id)
        return ResponseEntity.ok(response)
    }

}