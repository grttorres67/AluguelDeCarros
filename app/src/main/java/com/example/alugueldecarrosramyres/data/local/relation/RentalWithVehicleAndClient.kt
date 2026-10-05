package com.example.alugueldecarrosramyres.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.alugueldecarrosramyres.data.local.entity.ClientCacheEntity
import com.example.alugueldecarrosramyres.data.local.entity.RentalEntity
import com.example.alugueldecarrosramyres.data.local.entity.VehicleEntity

data class RentalWithVehicleAndClient(
    @Embedded
    val rental: RentalEntity,
    @Relation(
        parentColumn = "vehicleId",
        entityColumn = "id"
    )
    val vehicle: VehicleEntity,
    @Relation(
        parentColumn = "clientId",
        entityColumn = "id"
    )
    val client: ClientCacheEntity
)
