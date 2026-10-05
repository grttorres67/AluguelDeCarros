package com.example.alugueldecarrosramyres.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.alugueldecarrosramyres.data.local.relation.RentalWithVehicleAndClient
import com.example.alugueldecarrosramyres.ui.utils.ValidationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToNewRental: () -> Unit,
    onNavigateToVehicles: () -> Unit
) {
    val activeRentals by viewModel.activeRentals.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard - Locações Ativas") },
                actions = {
                    TextButton(onClick = onNavigateToVehicles) {
                        Text("Frota")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToNewRental) {
                Icon(Icons.Default.Add, contentDescription = "Nova Locação")
            }
        }
    ) { innerPadding ->
        if (activeRentals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.DirectionsCar,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Nenhuma locação ativa no momento.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(activeRentals) { item ->
                    RentalCard(item = item, onFinish = {
                        viewModel.finishRental(item.rental.id, item.vehicle.id)
                    })
                }
            }
        }
    }
}

@Composable
fun RentalCard(item: RentalWithVehicleAndClient, onFinish: () -> Unit) {
    val daysRemaining = ValidationUtils.calculateRemainingDays(item.rental.dataPrevistaEntrega)
    val isLate = daysRemaining < 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isLate) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant
        )
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
                    text = "${item.vehicle.marca} ${item.vehicle.modelo}",
                    style = MaterialTheme.typography.titleMedium
                )
                Surface(
                    color = if (isLate) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = if (isLate) "ATRASADO — ${-daysRemaining} dias" else "Dias restantes: $daysRemaining",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Placa: ${item.vehicle.placa}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Cliente: ${item.client.nome} (${item.client.telefone})", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Saída: ${ValidationUtils.formatDate(item.rental.dataSaida)}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Entrega Prevista: ${ValidationUtils.formatDate(item.rental.dataPrevistaEntrega)}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Valor Estimado: ${ValidationUtils.formatCurrency(item.rental.valorTotalEstimado)}", style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onFinish,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Finalizar Locação")
            }
        }
    }
}
