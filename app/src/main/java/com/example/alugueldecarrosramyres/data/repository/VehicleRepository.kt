package com.example.alugueldecarrosramyres.data.repository

import com.example.alugueldecarrosramyres.data.local.dao.VehicleDao
import com.example.alugueldecarrosramyres.data.local.entity.VehicleEntity
import com.example.alugueldecarrosramyres.data.remote.api.ApiService
import com.example.alugueldecarrosramyres.data.remote.dto.VehicleDto
import kotlinx.coroutines.flow.Flow

class VehicleRepository(
    private val vehicleDao: VehicleDao,
    private val apiService: ApiService
) {
    val allVehicles: Flow<List<VehicleEntity>> = vehicleDao.getAllVehicles()
    val availableVehicles: Flow<List<VehicleEntity>> = vehicleDao.getAvailableVehicles()

    suspend fun insertVehicle(vehicle: VehicleEntity): Long {
        val id = vehicleDao.insertVehicle(vehicle)
        try {
            apiService.syncVehicle(
                VehicleDto(
                    id = id,
                    marca = vehicle.marca,
                    modelo = vehicle.modelo,
                    placa = vehicle.placa,
                    ano = vehicle.ano,
                    valorDiaria = vehicle.valorDiaria,
                    status = vehicle.status
                )
            )
        } catch (_: Exception) {}
        return id
    }

    suspend fun getVehicleById(id: Long): VehicleEntity? {
        return vehicleDao.getVehicleById(id)
    }

    suspend fun updateStatus(id: Long, status: String) {
        vehicleDao.updateStatus(id, status)
    }
}
