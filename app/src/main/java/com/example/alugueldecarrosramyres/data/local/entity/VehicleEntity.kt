package com.example.alugueldecarrosramyres.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val marca: String,
    val modelo: String,
    val placa: String,
    val ano: Int,
    val valorDiaria: Double,
    val status: String // DISPONIVEL, ALUGADO, MANUTENCAO
)
