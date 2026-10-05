package com.example.alugueldecarrosramyres.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.alugueldecarrosramyres.data.local.database.AppDatabase
import com.example.alugueldecarrosramyres.data.local.relation.RentalWithVehicleAndClient
import com.example.alugueldecarrosramyres.data.remote.api.ApiService
import com.example.alugueldecarrosramyres.data.repository.RentalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val rentalRepository = RentalRepository(
        database = database,
        rentalDao = database.rentalDao(),
        vehicleDao = database.vehicleDao(),
        apiService = ApiService.create()
    )

    val activeRentals: StateFlow<List<RentalWithVehicleAndClient>> = rentalRepository.activeRentals
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun finishRental(rentalId: Long, vehicleId: Long) {
        viewModelScope.launch {
            rentalRepository.updateRentalStatus(rentalId, vehicleId, "FINALIZADA")
        }
    }
}
