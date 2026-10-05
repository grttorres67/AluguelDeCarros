package com.example.alugueldecarrosramyres.ui.vehicle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.alugueldecarrosramyres.data.local.entity.VehicleEntity
import com.example.alugueldecarrosramyres.ui.utils.ValidationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiclesScreen(
    viewModel: VehicleViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToNewVehicle: () -> Unit
) {
    val vehicles by viewModel.allVehicles.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Frota de Veículos") },
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
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToNewVehicle) {
                Icon(Icons.Default.Add, contentDescription = "Novo Veículo")
            }
        }
    ) { innerPadding ->
        if (vehicles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhum veículo cadastrado na frota.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(vehicles) { vehicle ->
                    VehicleCard(vehicle = vehicle)
                }
            }
        }
    }
}

@Composable
fun VehicleCard(vehicle: VehicleEntity) {
    val statusColor = when (vehicle.status) {
        "DISPONIVEL" -> Color(0xFF2E7D32)
        "ALUGADO" -> Color(0xFFC62828)
        else -> Color(0xFFEF6C00)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${vehicle.marca} ${vehicle.modelo}",
                    style = MaterialTheme.typography.titleMedium
                )
                Surface(
                    color = statusColor,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = vehicle.status,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Placa: ${vehicle.placa}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Ano: ${vehicle.ano}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Diária: ${ValidationUtils.formatCurrency(vehicle.valorDiaria)}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
