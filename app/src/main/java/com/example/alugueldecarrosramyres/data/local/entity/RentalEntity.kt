package com.example.alugueldecarrosramyres.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "rentals",
    foreignKeys = [
        ForeignKey(
            entity = VehicleEntity::class,
            parentColumns = ["id"],
            childColumns = ["vehicleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ClientCacheEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RentalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val vehicleId: Long,
    val clientId: Long,
    val dataSaida: Long,
    val dataPrevistaEntrega: Long,
    val valorTotalEstimado: Double,
    val status: String // ATIVA, FINALIZADA
)
