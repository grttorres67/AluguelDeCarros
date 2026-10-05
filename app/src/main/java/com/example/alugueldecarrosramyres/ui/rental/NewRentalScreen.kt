package com.example.alugueldecarrosramyres.ui.rental

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.alugueldecarrosramyres.ui.utils.ValidationUtils
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRentalScreen(
    viewModel: NewRentalViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToContacts: () -> Unit
) {
    val availableVehicles by viewModel.availableVehicles.collectAsStateWithLifecycle(initialValue = emptyList())
    val selectedVehicle by viewModel.selectedVehicle.collectAsStateWithLifecycle()
    val selectedClient by viewModel.selectedClient.collectAsStateWithLifecycle()
    val dataSaida by viewModel.dataSaida.collectAsStateWithLifecycle()
    val dataEntrega by viewModel.dataEntrega.collectAsStateWithLifecycle()
    val totalEstimado by viewModel.totalEstimado.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var showVehicleDialog by remember { mutableStateOf(false) }
    var showSaidaPicker by remember { mutableStateOf(false) }
    var showEntregaPicker by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.checkSelectedContact()
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collectLatest { success ->
            if (success) {
                onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova Locação") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Vehicle Selection
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showVehicleDialog = true }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Veículo Selecionado (Disponível)", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    if (selectedVehicle == null) {
                        Text(text = "Toque para escolher um veículo", color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text(text = "${selectedVehicle?.marca} ${selectedVehicle?.modelo} - Placa: ${selectedVehicle?.placa}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Diária: ${ValidationUtils.formatCurrency(selectedVehicle?.valorDiaria ?: 0.0)}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Client Selection
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToContacts() }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Cliente (Agenda)", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    if (selectedClient == null) {
                        Text(text = "Toque para selecionar da agenda", color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text(text = selectedClient?.nome ?: "", style = MaterialTheme.typography.titleMedium)
                        Text(text = selectedClient?.telefone ?: "", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Dates Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showSaidaPicker = true }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Data Saída", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = ValidationUtils.formatDate(dataSaida), style = MaterialTheme.typography.bodyMedium)
                    }
                }

                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showEntregaPicker = true }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Data Entrega Prevista", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = ValidationUtils.formatDate(dataEntrega), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Total Value Calculation
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Valor Total Estimado", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = ValidationUtils.formatCurrency(totalEstimado),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    val days = ValidationUtils.calculateDays(dataSaida, dataEntrega)
                    Text(text = "Quantidade de diárias: $days", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.confirmRental() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Locação")
            }
        }
    }

    // Vehicle Selection Dialog
    if (showVehicleDialog) {
        AlertDialog(
            onDismissRequest = { showVehicleDialog = false },
            title = { Text("Selecione um Veículo Disponível") },
            text = {
                if (availableVehicles.isEmpty()) {
                    Text("Nenhum veículo disponível no momento.")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableVehicles) { vehicle ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectVehicle(vehicle)
                                        showVehicleDialog = false
                                    },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = "${vehicle.marca} ${vehicle.modelo}", style = MaterialTheme.typography.titleSmall)
                                    Text(text = "Placa: ${vehicle.placa} | Diária: ${ValidationUtils.formatCurrency(vehicle.valorDiaria)}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showVehicleDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Date Picker Dialogs
    if (showSaidaPicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dataSaida)
        DatePickerDialog(
            onDismissRequest = { showSaidaPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.setDates(millis, dataEntrega)
                    }
                    showSaidaPicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaidaPicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showEntregaPicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dataEntrega)
        DatePickerDialog(
            onDismissRequest = { showEntregaPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.setDates(dataSaida, millis)
                    }
                    showEntregaPicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEntregaPicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
