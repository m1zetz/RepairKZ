package com.example.RepairKZ_Backend.entity

import com.example.RepairKZ_Backend.model.MasterServiceDTO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table

@Entity
@Table(name = "master_services")
data class MasterServiceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "master_id", nullable = false)
    var master: Master? = null,

    @Column(nullable = false)
    var service: String? = null,

    var price: Int = 0,
    var position: Int = 0
){
    fun toDto() : MasterServiceDTO{
        return MasterServiceDTO(
            id = this.id,
            masterId = this.master?.id?: 0,
            service = this.service?:"",
            price = this.price,
            position = this.position
        )
    }
}