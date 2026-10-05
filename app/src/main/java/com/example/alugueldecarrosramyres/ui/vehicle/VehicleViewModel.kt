package com.example.alugueldecarrosramyres.ui.vehicle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.alugueldecarrosramyres.data.local.database.AppDatabase
import com.example.alugueldecarrosramyres.data.local.entity.VehicleEntity
import com.example.alugueldecarrosramyres.data.remote.api.ApiService
import com.example.alugueldecarrosramyres.data.repository.VehicleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class VehicleViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val vehicleRepository = VehicleRepository(
        vehicleDao = database.vehicleDao(),
        apiService = ApiService.create()
    )

    val allVehicles: StateFlow<List<VehicleEntity>> = vehicleRepository.allVehicles
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val availableVehicles: StateFlow<List<VehicleEntity>> = vehicleRepository.availableVehicles
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
