package com.example.alugueldecarrosramyres.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alugueldecarrosramyres.ui.contacts.ContactsScreen
import com.example.alugueldecarrosramyres.ui.contacts.ContactsViewModel
import com.example.alugueldecarrosramyres.ui.dashboard.DashboardScreen
import com.example.alugueldecarrosramyres.ui.dashboard.DashboardViewModel
import com.example.alugueldecarrosramyres.ui.rental.NewRentalScreen
import com.example.alugueldecarrosramyres.ui.rental.NewRentalViewModel
import com.example.alugueldecarrosramyres.ui.rental.SelectedContactHolder
import com.example.alugueldecarrosramyres.ui.vehicle.NewVehicleScreen
import com.example.alugueldecarrosramyres.ui.vehicle.NewVehicleViewModel
import com.example.alugueldecarrosramyres.ui.vehicle.VehicleViewModel
import com.example.alugueldecarrosramyres.ui.vehicle.VehiclesScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val dashboardViewModel: DashboardViewModel = viewModel()
    val vehicleViewModel: VehicleViewModel = viewModel()
    val newVehicleViewModel: NewVehicleViewModel = viewModel()
    val contactsViewModel: ContactsViewModel = viewModel()
    val newRentalViewModel: NewRentalViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            DashboardScreen(
                viewModel = dashboardViewModel,
                onNavigateToNewRental = { navController.navigate("newRental") },
                onNavigateToVehicles = { navController.navigate("vehicles") }
            )
        }
        composable("vehicles") {
            VehiclesScreen(
                viewModel = vehicleViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToNewVehicle = { navController.navigate("newVehicle") }
            )
        }
        composable("newVehicle") {
            NewVehicleScreen(
                viewModel = newVehicleViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("contacts") {
            ContactsScreen(
                viewModel = contactsViewModel,
                onNavigateBack = { navController.popBackStack() },
                onContactSelected = { id, name, phone ->
                    SelectedContactHolder.set(id, name, phone)
                    navController.popBackStack()
                }
            )
        }
        composable("newRental") {
            NewRentalScreen(
                viewModel = newRentalViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToContacts = { navController.navigate("contacts") }
            )
        }
    }
}
