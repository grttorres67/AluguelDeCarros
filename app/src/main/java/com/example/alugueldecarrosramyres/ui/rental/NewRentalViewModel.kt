package com.example.alugueldecarrosramyres.ui.rental

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.alugueldecarrosramyres.data.local.database.AppDatabase
import com.example.alugueldecarrosramyres.data.local.entity.ClientCacheEntity
import com.example.alugueldecarrosramyres.data.local.entity.VehicleEntity
import com.example.alugueldecarrosramyres.data.remote.api.ApiService
import com.example.alugueldecarrosramyres.data.repository.ClientRepository
import com.example.alugueldecarrosramyres.data.repository.RentalRepository
import com.example.alugueldecarrosramyres.data.repository.VehicleRepository
import com.example.alugueldecarrosramyres.ui.utils.ValidationUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewRentalViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val vehicleRepository = VehicleRepository(database.vehicleDao(), ApiService.create())
    private val clientRepository = ClientRepository(database.clientDao())
    private val rentalRepository = RentalRepository(database, database.rentalDao(), database.vehicleDao(), ApiService.create())

    val availableVehicles: Flow<List<VehicleEntity>> = vehicleRepository.availableVehicles

    private val _selectedVehicle = MutableStateFlow<VehicleEntity?>(null)
    val selectedVehicle: StateFlow<VehicleEntity?> = _selectedVehicle.asStateFlow()

    private val _selectedClient = MutableStateFlow<ClientCacheEntity?>(null)
    val selectedClient: StateFlow<ClientCacheEntity?> = _selectedClient.asStateFlow()

    private val _dataSaida = MutableStateFlow(System.currentTimeMillis())
    val dataSaida: StateFlow<Long> = _dataSaida.asStateFlow()

    private val _dataEntrega = MutableStateFlow(System.currentTimeMillis() + 86400000L * 3)
    val dataEntrega: StateFlow<Long> = _dataEntrega.asStateFlow()

    private val _totalEstimado = MutableStateFlow(0.0)
    val totalEstimado: StateFlow<Double> = _totalEstimado.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Boolean>()
    val navigationEvent: SharedFlow<Boolean> = _navigationEvent.asSharedFlow()

    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage: SharedFlow<String> = _uiMessage.asSharedFlow()

    init {
        checkSelectedContact()
    }

    fun checkSelectedContact() {
        val id = SelectedContactHolder.contactId
        val name = SelectedContactHolder.name
        val phone = SelectedContactHolder.phone
        if (!id.isNullOrBlank() && !name.isNullOrBlank() && !phone.isNullOrBlank()) {
            selectClient(id, name, phone)
            SelectedContactHolder.clear()
        }
    }

    fun selectVehicle(vehicle: VehicleEntity) {
        _selectedVehicle.value = vehicle
        recalculateTotal()
    }

    fun selectClient(contactId: String, nome: String, telefone: String) {
        viewModelScope.launch {
            val client = ClientCacheEntity(
                contactId = contactId,
                nome = nome,
                telefone = telefone
            )
            val id = clientRepository.insertOrUpdateClient(client)
            val savedClient = clientRepository.getClientById(id)
            _selectedClient.value = savedClient
        }
    }

    fun setDates(saida: Long, entrega: Long) {
        if (entrega < saida) {
            viewModelScope.launch { _uiMessage.emit("A data de entrega não pode ser anterior à data de saída.") }
            return
        }
        _dataSaida.value = saida
        _dataEntrega.value = entrega
        recalculateTotal()
    }

    private fun recalculateTotal() {
        val vehicle = _selectedVehicle.value
        if (vehicle != null) {
            val days = ValidationUtils.calculateDays(_dataSaida.value, _dataEntrega.value)
            _totalEstimado.value = days * vehicle.valorDiaria
        } else {
            _totalEstimado.value = 0.0
        }
    }

    fun confirmRental() {
        val vehicle = _selectedVehicle.value
        val client = _selectedClient.value
        if (vehicle == null) {
            viewModelScope.launch { _uiMessage.emit("Selecione um veículo disponível.") }
            return
        }
        if (client == null) {
            viewModelScope.launch { _uiMessage.emit("Selecione um cliente da agenda.") }
            return
        }
        if (_dataEntrega.value < _dataSaida.value) {
            viewModelScope.launch { _uiMessage.emit("Intervalo de datas inválido.") }
            return
        }

        viewModelScope.launch {
            try {
                val days = ValidationUtils.calculateDays(_dataSaida.value, _dataEntrega.value)
                val total = days * vehicle.valorDiaria
                rentalRepository.createRental(
                    vehicleId = vehicle.id,
                    clientId = client.id,
                    dataSaida = _dataSaida.value,
                    dataPrevistaEntrega = _dataEntrega.value,
                    valorTotal = total
                )
                _navigationEvent.emit(true)
            } catch (e: Exception) {
                _uiMessage.emit("Erro ao criar locação: ${e.localizedMessage}")
            }
        }
    }
}
