package com.example.alugueldecarrosramyres.ui.vehicle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.alugueldecarrosramyres.data.local.database.AppDatabase
import com.example.alugueldecarrosramyres.data.local.entity.VehicleEntity
import com.example.alugueldecarrosramyres.data.remote.api.ApiService
import com.example.alugueldecarrosramyres.data.repository.VehicleRepository
import com.example.alugueldecarrosramyres.ui.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NewVehicleViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val vehicleRepository = VehicleRepository(
        vehicleDao = database.vehicleDao(),
        apiService = ApiService.create()
    )

    private val _navigationEvent = MutableSharedFlow<Boolean>()
    val navigationEvent: SharedFlow<Boolean> = _navigationEvent.asSharedFlow()

    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage: SharedFlow<String> = _uiMessage.asSharedFlow()

    fun saveVehicle(marca: String, modelo: String, placa: String, anoStr: String, diariaStr: String) {
        if (marca.isBlank() || modelo.isBlank() || placa.isBlank() || anoStr.isBlank() || diariaStr.isBlank()) {
            viewModelScope.launch { _uiMessage.emit("Preencha todos os campos obrigatórios.") }
            return
        }

        if (!ValidationUtils.isValidPlate(placa)) {
            viewModelScope.launch { _uiMessage.emit("Placa inválida! Use o formato AAA-1234 ou AAA1A23.") }
            return
        }

        val ano = anoStr.toIntOrNull()
        if (ano == null || ano < 1900 || ano > 2100) {
            viewModelScope.launch { _uiMessage.emit("Ano inválido.") }
            return
        }

        if (!ValidationUtils.isValidDailyRate(diariaStr)) {
            viewModelScope.launch { _uiMessage.emit("Valor da diária deve ser um número positivo.") }
            return
        }

        val diaria = diariaStr.replace(",", ".").toDouble()

        viewModelScope.launch {
            try {
                val vehicle = VehicleEntity(
                    marca = marca.trim(),
                    modelo = modelo.trim(),
                    placa = placa.trim().uppercase(),
                    ano = ano,
                    valorDiaria = diaria,
                    status = "DISPONIVEL"
                )
                vehicleRepository.insertVehicle(vehicle)
                _navigationEvent.emit(true)
            } catch (e: Exception) {
                _uiMessage.emit("Erro ao salvar veículo: ${e.localizedMessage}")
            }
        }
    }
}
