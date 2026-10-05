package com.example.alugueldecarrosramyres.data.repository

import androidx.room.withTransaction
import com.example.alugueldecarrosramyres.data.local.dao.RentalDao
import com.example.alugueldecarrosramyres.data.local.dao.VehicleDao
import com.example.alugueldecarrosramyres.data.local.database.AppDatabase
import com.example.alugueldecarrosramyres.data.local.entity.RentalEntity
import com.example.alugueldecarrosramyres.data.local.relation.RentalWithVehicleAndClient
import com.example.alugueldecarrosramyres.data.remote.api.ApiService
import com.example.alugueldecarrosramyres.data.remote.dto.RentalDto
import kotlinx.coroutines.flow.Flow

class RentalRepository(
    private val database: AppDatabase,
    private val rentalDao: RentalDao,
    private val vehicleDao: VehicleDao,
    private val apiService: ApiService
) {
    val activeRentals: Flow<List<RentalWithVehicleAndClient>> = rentalDao.getActiveRentalsWithDetails()

    suspend fun createRental(
        vehicleId: Long,
        clientId: Long,
        dataSaida: Long,
        dataPrevistaEntrega: Long,
        valorTotal: Double
    ): Long {
        var rentalId = 0L
        database.withTransaction {
            val rental = RentalEntity(
                vehicleId = vehicleId,
                clientId = clientId,
                dataSaida = dataSaida,
                dataPrevistaEntrega = dataPrevistaEntrega,
                valorTotalEstimado = valorTotal,
                status = "ATIVA"
            )
            rentalId = rentalDao.insertRental(rental)
            vehicleDao.updateStatus(vehicleId, "ALUGADO")
        }
        try {
            apiService.syncRental(
                RentalDto(
                    id = rentalId,
                    vehicleId = vehicleId,
                    clientId = clientId,
                    dataSaida = dataSaida,
                    dataPrevistaEntrega = dataPrevistaEntrega,
                    valorTotalEstimado = valorTotal,
                    status = "ATIVA"
                )
            )
        } catch (_: Exception) {}
        return rentalId
    }

    suspend fun updateRentalStatus(rentalId: Long, vehicleId: Long, newStatus: String) {
        database.withTransaction {
            rentalDao.updateRentalStatus(rentalId, newStatus)
            if (newStatus == "FINALIZADA") {
                vehicleDao.updateStatus(vehicleId, "DISPONIVEL")
            }
        }
    }
}
